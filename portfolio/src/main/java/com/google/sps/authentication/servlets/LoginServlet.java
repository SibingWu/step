package com.google.sps.authentication.servlets;

import com.google.appengine.api.users.UserServiceFactory;
import com.google.sps.authentication.userservice.UserServiceInteraction;
import com.google.sps.utils.ServletUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/login")
public final class LoginServlet extends HttpServlet {

    private UserServiceInteraction userServiceInteraction;

    private class ResultType {
        // For json creation
        private boolean isLoggedIn;
        private String htmlText;

        private ResultType(boolean isLoggedIn, String htmlText) {
            this.isLoggedIn = isLoggedIn;
            this.htmlText = htmlText;
        }
    }

    @Override
    public void init() {
        this.userServiceInteraction = new UserServiceInteraction(UserServiceFactory.getUserService());
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;");

        boolean isUserLoggedIn = this.userServiceInteraction.isUserLoggedIn();
        String htmlText;

        if (isUserLoggedIn) {
            String userEmail = this.userServiceInteraction.getUserEmail();
            String urlToRedirectToAfterUserLogsOut = "/index.html";
            String logoutUrl = this.userServiceInteraction.createLogoutURL(urlToRedirectToAfterUserLogsOut);

            htmlText = String.format("<p>Hello %s!</p>\n"
                    + "<p>Logout <a href=\"%s\">here</a>.</p>", userEmail, logoutUrl);
        } else {
            String urlToRedirectToAfterUserLogsIn = "/comments.html";
            String loginUrl = this.userServiceInteraction.createLoginURL(urlToRedirectToAfterUserLogsIn);

            htmlText = String.format("<p>Hello stranger.</p>"
                    + "<p>Login <a href=\"%s\">here</a>.</p>", loginUrl);
        }

        ResultType resultType = new ResultType(isUserLoggedIn, htmlText);
        response.getWriter().println(ServletUtils.convertToJsonUsingGson(resultType));
    }
}
