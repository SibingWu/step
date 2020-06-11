package com.google.sps.authentication.servlets;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private UserService userService;

    private class ResultType {
        private boolean isLoggedIn;
        private String htmlText;

        private ResultType(boolean isLoggedIn, String htmlText) {
            this.isLoggedIn = isLoggedIn;
            this.htmlText = htmlText;
        }
    }

    @Override
    public void init() {
        this.userService = UserServiceFactory.getUserService();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;");

        boolean isLoggedIn = this.userService.isUserLoggedIn();
        String htmlText;

        if (isLoggedIn) {
            String userEmail = userService.getCurrentUser().getEmail();
            String urlToRedirectToAfterUserLogsOut = "/login";
            String logoutUrl = userService.createLogoutURL(urlToRedirectToAfterUserLogsOut);

            htmlText = String.format("<p>Hello %s!</p>\n"
                    + "<p>Logout <a href=\"%s\">here</a>.</p>", userEmail, logoutUrl);
        } else {
            String urlToRedirectToAfterUserLogsIn = "/login";
            String loginUrl = userService.createLoginURL(urlToRedirectToAfterUserLogsIn);

            htmlText = String.format("<p>Hello stranger.</p>"
                    + "<p>Login <a href=\"%s\">here</a>.</p>", loginUrl);
        }

        ResultType resultType = new ResultType(isLoggedIn, htmlText);
        response.getWriter().println(convertToJsonUsingGson(resultType));
    }

    private String convertToJsonUsingGson(ResultType resultType) {
        Gson gson = new Gson();
        String json = gson.toJson(resultType);
        return json;
    }
}
