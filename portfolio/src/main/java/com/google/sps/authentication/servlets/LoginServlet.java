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

    @Override
    public void init() {
        this.userService = UserServiceFactory.getUserService();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;");

        boolean isLoggedIn = this.userService.isUserLoggedIn();
        response.getWriter().println(convertToJsonUsingGson(isLoggedIn));

        if (isLoggedIn) {
            String userEmail = userService.getCurrentUser().getEmail();
            String urlToRedirectToAfterUserLogsOut = "/login";
            String logoutUrl = userService.createLogoutURL(urlToRedirectToAfterUserLogsOut);

            response.getWriter().println("<p>Hello " + userEmail + "!</p>");
            response.getWriter().println("<p>Logout <a href=\"" + logoutUrl + "\">here</a>.</p>");
        } else {
            String urlToRedirectToAfterUserLogsIn = "/login";
            String loginUrl = userService.createLoginURL(urlToRedirectToAfterUserLogsIn);

            response.getWriter().println("<p>Hello stranger.</p>");
            response.getWriter().println("<p>Login <a href=\"" + loginUrl + "\">here</a>.</p>");
        }
    }

    private String convertToJsonUsingGson(boolean isLoggedIn) {
        Gson gson = new Gson();
        String json = gson.toJson(isLoggedIn);
        return json;
    }
}
