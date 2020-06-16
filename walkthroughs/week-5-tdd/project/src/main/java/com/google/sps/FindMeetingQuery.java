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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    Collection<String> attendees = request.getAttendees();
    Collection<String> optionalAttendees = request.getOptionalAttendees();
    long duration = request.getDuration();

    List<TimeRange> occupiedTimeRange = new ArrayList<>();
    List<TimeRange> occupiedTimeRangeWithOptional = new ArrayList<>();

    for (Event event: events) {
      // Ignores those events which do not have overlapping attendees
      if (!hasAttendeesAttending(event.getAttendees(), attendees)
              && !hasAttendeesAttending(event.getAttendees(), optionalAttendees)) {
        continue;
      }

      occupiedTimeRange.add(event.getWhen());

      if (!hasAttendeesAttending(event.getAttendees(), optionalAttendees)) {
        continue;
      }

      occupiedTimeRangeWithOptional.add(event.getWhen());
    }

    Collection<TimeRange> availableMeetings = getAvailableTimeRange(occupiedTimeRange, duration);

    if (availableMeetings != null) {
      return availableMeetings;
    } else {
      return getAvailableTimeRange(occupiedTimeRangeWithOptional, duration);
    }
  }

  private boolean hasAttendeesAttending(Collection<String> eventAttendees, Collection<String> requestAttendees) {
    for (String attendee: requestAttendees) {
      if (eventAttendees.contains(attendee)) {
        return true;
      }
    }

    return false;
  }

  private List<TimeRange> mergeTimeRange(List<TimeRange> timeRanges) {
    List<TimeRange> mergedTimeRanges = new ArrayList<>();

    if (timeRanges == null) {
      return mergedTimeRanges;
    }

    Collections.sort(timeRanges, TimeRange.ORDER_BY_START);

    TimeRange last = null;
    for (TimeRange timeRange: timeRanges) {
      // The first TimeRange or no overlapping
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

  private Collection<TimeRange> getAvailableTimeRange(List<TimeRange> occupiedTimeRange, long duration) {
    Collection<TimeRange> availableMeetings = new ArrayList<>();

    List<TimeRange> mergedTimeRanges = mergeTimeRange(occupiedTimeRange);

    int start = TimeRange.START_OF_DAY;

    for (TimeRange timeRange: mergedTimeRanges) {
      // Interval duration is not enough
      if (timeRange.start() - start < duration) {
        start = timeRange.contains(timeRange.end()) ?
                Math.min(timeRange.end() + 1, TimeRange.END_OF_DAY) : timeRange.end();
        continue;
      }

      TimeRange availableMeeting = TimeRange.fromStartEnd(start, timeRange.start(), false);
      availableMeetings.add(availableMeeting);

      start = timeRange.contains(timeRange.end()) ?
              Math.min(timeRange.end() + 1, TimeRange.END_OF_DAY) : timeRange.end();
    }

    // Deals with the last remaining time slot of a day
    if (start < TimeRange.END_OF_DAY && (TimeRange.END_OF_DAY - start) >= duration) {
      availableMeetings.add(TimeRange.fromStartEnd(start, TimeRange.END_OF_DAY, true));
    }

    return availableMeetings;
  }
}
