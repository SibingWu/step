package com.google.sps.authentication.servlets;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/login")
public final class LoginServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init() {
        this.userService = UserServiceFactory.getUserService();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;");

        response.getWriter().println(this.userService.isUserLoggedIn());
    }
}
