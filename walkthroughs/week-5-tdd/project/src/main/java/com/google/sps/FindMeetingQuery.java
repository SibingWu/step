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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class FindMeetingQuery {
  /**
   * Gets the available time slot for new meeting given existing meeting events.
   * @param events Existing meeting events, assuming all events end before EOD.
   * @param request Request to set up a new meeting
   * @return If one or more time slots exists so that both mandatory and optional attendees can attend,
   *         returns those time slots. Otherwise, returns the time slots that fit just the mandatory attendees.
   *         Returns empty if duration is greater than a day.
   */
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    long duration = request.getDuration();
    if (duration > TimeRange.WHOLE_DAY.duration() || duration <= 0) {
      return Collections.unmodifiableList(new ArrayList<>());
    }

    Set<String> attendees = new HashSet<>(request.getAttendees());
    Set<String> attendeesWithOptional = new HashSet<>();
    attendeesWithOptional.addAll(request.getAttendees());
    attendeesWithOptional.addAll(request.getOptionalAttendees());

    // No attendees at all.
    if (attendeesWithOptional.isEmpty()) {
      return Collections.unmodifiableList(Arrays.asList(TimeRange.WHOLE_DAY));
    }

    List<TimeRange> occupiedTimeRange = new ArrayList<>();
    List<TimeRange> occupiedTimeRangeWithOptional = new ArrayList<>();

    for (Event event: events) {
      getOccupiedTimeRange(attendees, occupiedTimeRange, event);

      getOccupiedTimeRange(attendeesWithOptional, occupiedTimeRangeWithOptional, event);
    }

    List<TimeRange> availableMeetings = getAvailableTimeRange(occupiedTimeRangeWithOptional, duration);

    // Has time slots that satisfy both mandatory and optional attendees.
    if (!availableMeetings.isEmpty()) {
      return Collections.unmodifiableList(availableMeetings);
    }

    // No mandatory attendees.
    if (attendees.isEmpty()) {
      Collections.unmodifiableList(new ArrayList<>());
    }

    return Collections.unmodifiableList(getAvailableTimeRange(occupiedTimeRange, duration));
  }

  private void getOccupiedTimeRange(Set<String> attendees, List<TimeRange> occupiedTimeRange, Event event) {
    if (hasAttendeesAttending(event.getAttendees(), attendees)) {
      occupiedTimeRange.add(event.getWhen());
    }
  }

  private boolean hasAttendeesAttending(Set<String> eventAttendees, Set<String> requestAttendees) {
    for (String attendee: requestAttendees) {
      if (eventAttendees.contains(attendee)) {
        return true;
      }
    }

    return false;
  }

  private List<TimeRange> getAvailableTimeRange(List<TimeRange> occupiedTimeRange, long duration) {
    // Assumes 0 < duration <= WHOLE_DAY

    List<TimeRange> availableMeetings = new ArrayList<>();

    if (occupiedTimeRange.isEmpty()) {
      availableMeetings.add(TimeRange.WHOLE_DAY);
      return availableMeetings;
    }

    List<TimeRange> mergedTimeRanges = mergeTimeRange(occupiedTimeRange);

    int start = TimeRange.START_OF_DAY;

    for (TimeRange timeRange: mergedTimeRanges) {
      // Interval duration is not enough.
      if ((timeRange.start() - start) < duration) {
        int startForInclusive = Math.min(timeRange.end() + 1, TimeRange.END_OF_DAY);
        int startForNonInclusive = Math.min(timeRange.end(), TimeRange.END_OF_DAY);
        start = timeRange.contains(timeRange.end()) ? startForInclusive : startForNonInclusive;
        continue;
      }

      TimeRange availableMeeting = TimeRange.fromStartEnd(start, timeRange.start(), /* inclusive= */false);
      availableMeetings.add(availableMeeting);

      int startForInclusive = Math.min(timeRange.end() + 1, TimeRange.END_OF_DAY);
      int startForNonInclusive = Math.min(timeRange.end(), TimeRange.END_OF_DAY);
      start = timeRange.contains(timeRange.end()) ? startForInclusive : startForNonInclusive;
    }

    // Deals with the last remaining time slot of a day.
    if (start < TimeRange.END_OF_DAY && (TimeRange.END_OF_DAY - start) >= duration) {
      availableMeetings.add(TimeRange.fromStartEnd(start, TimeRange.END_OF_DAY, /* inclusive= */true));
    }

    return availableMeetings;
  }

  private List<TimeRange> mergeTimeRange(List<TimeRange> timeRanges) {
    List<TimeRange> mergedTimeRanges = new ArrayList<>();

    if (timeRanges.isEmpty()) {
      return mergedTimeRanges;
    }

    Collections.sort(timeRanges, TimeRange.ORDER_BY_START);

    TimeRange last = null;
    for (TimeRange timeRange: timeRanges) {
      // The first TimeRange or no overlapping.
      if (last == null || !timeRange.overlaps(last)) {
        mergedTimeRanges.add(timeRange);
        last = timeRange;
      } else {
        TimeRange mergedTimeRange;
        if (last.contains(timeRange)) {
          continue;
        }

        if (last.end() == timeRange.end()) {
          mergedTimeRange = TimeRange.fromStartEnd(last.start(), last.end(),
                  last.contains(last.end()) || timeRange.contains(timeRange.end()));

        } else {
          mergedTimeRange = TimeRange.fromStartEnd(last.start(), timeRange.end(), timeRange.contains(timeRange.end()));
        }

        mergedTimeRanges.remove(mergedTimeRanges.size() - 1);
        mergedTimeRanges.add(mergedTimeRange);
        last = mergedTimeRange;
      }
    }

    return mergedTimeRanges;
  }
}
