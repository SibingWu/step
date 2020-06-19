package com.google.sps;

import java.util.Set;

public final class Utils {
    private Utils() {};

    public static boolean hasIntersection(Set<String> eventAttendees, Set<String> requestAttendees) {
        for (String attendee: requestAttendees) {
            if (eventAttendees.contains(attendee)) {
                return true;
            }
        }

        return false;
    }
}
