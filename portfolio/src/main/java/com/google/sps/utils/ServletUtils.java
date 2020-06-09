package com.google.sps.utils;

import javax.servlet.http.HttpServletRequest;

/** Auxiliary functions and variables to support comment feature. */
public class ServletUtils {
    // Makes it non-initializable
    private ServletUtils() {}

    /**
     * @return the value of parameter with the {@code name} in the {@code request}
     *         or returns {@code defaultValue} if that parameter does not exist.
     */
    public static String getParameter(HttpServletRequest request, String name, String defaultValue) {
        String value = request.getParameter(name);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }
}
