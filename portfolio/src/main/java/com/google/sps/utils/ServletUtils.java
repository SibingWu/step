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

    /**
     * Gets the integer parameter from html form input.
     * @param request Http request.
     * @param parameterName Input id in html element.
     * @param defaultValue Default integer.
     * @return Parsed integer or default value if exception occur
     */
    public static int getIntParameter(HttpServletRequest request, String parameterName, int defaultValue) {
        String resultStr = request.getParameter(parameterName);

        try {
            int result = Integer.parseInt(resultStr);

            if (result < 0 || result > 10) {
                throw new NumberFormatException("Please enter an integer between 1 and 10.");
            }

            return result;
        } catch (NumberFormatException e) {
            System.err.println("Could not convert to int: " + resultStr);
            // TODO: add logging to log the error

            return defaultValue;
        }
    }
}
