package com.google.sps.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
     * @param defaultValue Default integer string.
     * @return Parsed integer or -1.
     */
    public static int getIntParameter(HttpServletRequest request, String parameterName, String defaultValue) {
        String maxNumOfCommentStr = ServletUtils.getParameter(
                request, parameterName, defaultValue);

        int result = -1;

        try {
            result = Integer.parseInt(maxNumOfCommentStr);
        } catch (NumberFormatException e) {
            System.err.println("Could not convert to int: " + maxNumOfCommentStr);

            return result;
        }

        return result;
    }
}
