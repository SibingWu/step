// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import com.google.appengine.repackaged.com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Class that handles finding eligible time slot.
 */
public final class FindMeetingQuery {
  /**
   * Gets the available time slot for new meeting given existing meeting events.
   * @param events Existing meeting events, assuming all events end before EOD.
   * @param request Request to set up a new meeting with duration not exceeding the whole day.
   * @return If one or more time slots exists so that both mandatory and optional attendees can attend,
   *         returns those time slots. Otherwise, returns the time slots that fit just the mandatory attendees.
   *         Returns empty collection if invalid input.
   */
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    long durationInMinutes = request.getDuration();
    if (!isValidParams(events, durationInMinutes)) {
      return ImmutableList.of();
    }

    Set<String> attendees = new HashSet<>(request.getAttendees());
    Set<String> attendeesWithOptional = new HashSet<>();
    attendeesWithOptional.addAll(request.getAttendees());
    attendeesWithOptional.addAll(request.getOptionalAttendees());

    // No attendees at all.
    if (attendeesWithOptional.isEmpty()) {
      return ImmutableList.of(TimeRange.WHOLE_DAY);
    }

    ImmutableList<TimeRange> occupiedTimeRange = getOccupiedTimeRange(attendees, events);
    ImmutableList<TimeRange> occupiedTimeRangeWithOptional = getOccupiedTimeRange(attendeesWithOptional, events);

    ImmutableList<TimeRange> availableMeetings = getAvailableTimeRange(occupiedTimeRangeWithOptional, durationInMinutes);

    // Has time slots that satisfy both mandatory and optional attendees.
    if (!availableMeetings.isEmpty()) {
      return availableMeetings;
    }

    // No mandatory attendees.
    if (attendees.isEmpty()) {
      return ImmutableList.of();
    }

    return getAvailableTimeRange(occupiedTimeRange, durationInMinutes);
  }

  private boolean isValidParams(Collection<Event> events, long duration) {
    // TODO: check for events that all events not crossing EOD.

    if (duration > TimeRange.WHOLE_DAY.duration() || duration <= 0) {
      return false;
    }

    return true;
  }

  private ImmutableList<TimeRange> getOccupiedTimeRange(Set<String> attendees, Collection<Event> events) {
    ImmutableList.Builder<TimeRange> occupiedTimeRange = ImmutableList.builder();
    for (Event event: events) {
      if (hasIntersection(event.getAttendees(), attendees)) {
        occupiedTimeRange.add(event.getWhen());
      }
    }
    return occupiedTimeRange.build();
  }

  private ImmutableList<TimeRange> getAvailableTimeRange(List<TimeRange> occupiedTimeRange, long duration) {
    // Assumes 0 < duration <= WHOLE_DAY

    if (occupiedTimeRange.isEmpty()) {
      return ImmutableList.of(TimeRange.WHOLE_DAY);
    }

    ImmutableList<TimeRange> mergedTimeRanges = mergeTimeRange(occupiedTimeRange);

    ImmutableList.Builder<TimeRange> availableMeetings = ImmutableList.builder();
    int start = TimeRange.START_OF_DAY;

    for (TimeRange timeRange: mergedTimeRanges) {
      // Interval duration is not enough.
      if ((timeRange.start() - start) < duration) {
        start = Math.min(timeRange.end(), TimeRange.END_OF_DAY);
        continue;
      }

      TimeRange availableMeeting = TimeRange.fromStartEnd(start, timeRange.start(), /* inclusive= */false);
      availableMeetings.add(availableMeeting);

      start = Math.min(timeRange.end(), TimeRange.END_OF_DAY);
    }

    // Deals with the last remaining time slot of a day.
    if (start < TimeRange.END_OF_DAY && (TimeRange.END_OF_DAY - start) >= duration) {
      availableMeetings.add(TimeRange.fromStartEnd(start, TimeRange.END_OF_DAY, /* inclusive= */true));
    }

    return availableMeetings.build();
  }

  private ImmutableList<TimeRange> mergeTimeRange(List<TimeRange> timeRanges) {
    if (timeRanges.isEmpty()) {
      return ImmutableList.of();
    }

    List<TimeRange> mergedTimeRanges = new ArrayList<>();

    ImmutableList<TimeRange> sortedTimeRanges = ImmutableList.sortedCopyOf(TimeRange.ORDER_BY_START, timeRanges);

    TimeRange last = null;
    for (TimeRange timeRange: sortedTimeRanges) {
      // The first TimeRange or no overlapping.
      if (last == null || !timeRange.overlaps(last)) {
        mergedTimeRanges.add(timeRange);
        last = timeRange;
      } else {
        if (last.contains(timeRange)) {
          continue;
        }

        TimeRange mergedTimeRange = TimeRange.fromStartEnd(last.start(), timeRange.end(), /* inclusive= */false);

        // Replaces the last time range with the newly merged time range.
        mergedTimeRanges.remove(mergedTimeRanges.size() - 1);
        mergedTimeRanges.add(mergedTimeRange);
        last = mergedTimeRange;
      }
    }

    return ImmutableList.copyOf(mergedTimeRanges);
  }

  private static boolean hasIntersection(Set<String> eventAttendees, Set<String> requestAttendees) {
    return !Collections.disjoint(eventAttendees, requestAttendees);
  }
}
