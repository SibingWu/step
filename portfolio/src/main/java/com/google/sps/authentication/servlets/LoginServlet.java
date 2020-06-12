package com.google.sps.authentication.servlets;

import com.google.appengine.api.users.UserServiceFactory;
import com.google.sps.authentication.userservice.UserServiceInteraction;
import com.google.sps.utils.ServletUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/** Gets the log in information. */
@WebServlet("/login")
public final class LoginServlet extends HttpServlet {
    private static final String GUEST_USER_NAME = "stranger";
    private static final String URL_TO_REDIRECT_TO_AFTER_LOGS_OUT = "/index.html";
    private static final String URL_TO_REDIRECT_TO_AFTER_LOGS_IN = "/comments.html";

    private UserServiceInteraction userServiceInteraction;

    private class LoginResult {
        private boolean isLoggedIn;
        private String loggingUrl;
        private String user;

        private LoginResult(boolean isLoggedIn, String loggingUrl, String user) {
            this.isLoggedIn = isLoggedIn;
            this.loggingUrl = loggingUrl;
            this.user = user;
        }
    }

    @Override
    public void init() {
        this.userServiceInteraction = new UserServiceInteraction(UserServiceFactory.getUserService());
    }

    // Gets the log in status.
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;");

        boolean isLoggedIn = this.userServiceInteraction.isUserLoggedIn();
        String loggingUrl;
        String user;

        if (isLoggedIn) {
            user = this.userServiceInteraction.getUserEmail();
            String urlToRedirectToAfterUserLogsOut = URL_TO_REDIRECT_TO_AFTER_LOGS_OUT;
            loggingUrl = this.userServiceInteraction.createLogoutURL(urlToRedirectToAfterUserLogsOut);
        } else {
            user = GUEST_USER_NAME;
            String urlToRedirectToAfterUserLogsIn = URL_TO_REDIRECT_TO_AFTER_LOGS_IN;
            loggingUrl = this.userServiceInteraction.createLoginURL(urlToRedirectToAfterUserLogsIn);
        }

        LoginResult loginResult = new LoginResult(isLoggedIn, loggingUrl, user);
        response.getWriter().println(ServletUtils.convertToJsonUsingGson(loginResult));
    }
}
