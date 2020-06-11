package com.google.sps.authentication.servlets;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

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
        response.setContentType("text/plain;");

        response.getWriter().println(this.userService.isUserLoggedIn());

        response.setContentType("text/html;");
        if (userService.isUserLoggedIn()) {
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
}
