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
    // throw new UnsupportedOperationException("TODO: Implement this method.");

    Collection<TimeRange> availableMeetings = new ArrayList<>();

    Collection<String> attendees = request.getAttendees();
    long duration = request.getDuration();

    List<TimeRange> occupiedTimeRange = new ArrayList<>();

    for (Event event: events) {
      // Ignores those events which do not have overlapping attendees
      if (!hasAttendeesAttending(event.getAttendees(), attendees)) {
        continue;
      }

      occupiedTimeRange.add(event.getWhen());
    }

    List<TimeRange> mergedTimeRanges = mergeTimeRange(occupiedTimeRange);

    int start = TimeRange.START_OF_DAY;
    int end = TimeRange.START_OF_DAY;
    for (TimeRange timeRange: mergedTimeRanges) {
      if (timeRange.start() - start < duration) {
        start = timeRange.contains(end) ? end - 1 : end;
        end = start;
        continue;
      }

      TimeRange availableMeeting = TimeRange.fromStartEnd(start, timeRange.end(), timeRange.contains(end));
      availableMeetings.add(availableMeeting);

      start = timeRange.contains(end) ? end - 1 : end;
      end = start;
    }

    return availableMeetings;
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
}
