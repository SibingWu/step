package com.google.sps.authentication.servlets;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;

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

    private UserService userService;

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
        this.userService = UserServiceFactory.getUserService();
    }

    // Gets the log in status.
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;");

        boolean isLoggedIn = this.userService.isUserLoggedIn();
        String loggingUrl;
        String user;

        if (isLoggedIn) {
            user = userService.getCurrentUser().getEmail();
            String urlToRedirectToAfterUserLogsOut = URL_TO_REDIRECT_TO_AFTER_LOGS_OUT;
            loggingUrl = userService.createLogoutURL(urlToRedirectToAfterUserLogsOut);
        } else {
            user = GUEST_USER_NAME;
            String urlToRedirectToAfterUserLogsIn = URL_TO_REDIRECT_TO_AFTER_LOGS_IN;
            loggingUrl = userService.createLoginURL(urlToRedirectToAfterUserLogsIn);
        }

        LoginResult loginResult = new LoginResult(isLoggedIn, loggingUrl, user);
        response.getWriter().println(convertToJsonUsingGson(loginResult));
    }

    private String convertToJsonUsingGson(LoginResult loginResult) {
        Gson gson = new Gson();
        String json = gson.toJson(loginResult);
        return json;
    }
}
