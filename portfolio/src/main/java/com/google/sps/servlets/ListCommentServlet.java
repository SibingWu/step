package com.google.sps.servlets;

import com.google.appengine.api.datastore.*;
import com.google.gson.Gson;
import com.google.sps.data.Comment;
import com.google.sps.utils.CommentDataStore;
import com.google.sps.utils.CommentUtils;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.List;


/** Servlet that handles posting comment content. */
@WebServlet("/list-comment")
public class ListCommentServlet extends HttpServlet {
    private int maxNumberOfComments;

    static class Constants {
        final static String MAXNUMBER = "quantity";
    }

    @Override
    public void init() {
        this.maxNumberOfComments = 0; // default value
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Gets the displayed comment limit
        getMaxNumberOfComments(request, response);

        // Loads the comment from Datastore
        List<Comment> comments = getComments(request);

        // Converts into json form
        String json = convertToJsonUsingGson(comments);

        // Sends the JSON as the response
        sendJsonResponse(response, json);
    }

    private void getMaxNumberOfComments(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String maxNumOfCommentStr = CommentUtils.getParameter(
                request, /*name=*/Constants.MAXNUMBER, /*defaultValue=*/"10");

        try {
            this.maxNumberOfComments = Integer.parseInt(maxNumOfCommentStr);

            if (this.maxNumberOfComments < 1 || this.maxNumberOfComments > 10) {
                response.setContentType("text/html;");
                response.getWriter().println("Please enter an integer between 1 and 10.");
            }
        } catch (NumberFormatException e) {
            System.err.println("Could not convert to int: " + maxNumOfCommentStr);
            response.setContentType("text/html;");
            response.getWriter().println("Please enter an integer between 1 and 10.");
        }

        response.sendRedirect("/index.html");
    }

    /** Loads the comment from Datastore */
    private List<Comment> getComments(HttpServletRequest request) {
        // Loads comments from Datastore
        Iterable<Entity> entityIterable = CommentDataStore.load();

        List<Comment> comments = new ArrayList<>();
        for (Entity entity: entityIterable) {
            Comment comment = Comment.CREATOR.fromEntity(entity);
            comments.add(comment);
        }
        return comments;
    }

    /** Converts the list of Comment objecct into json form */
    private String convertToJsonUsingGson(List<Comment> comments) {
        Gson gson = new Gson();
        String json = gson.toJson(comments);
        return json;
    }

    /** Sends the JSON as response */
    private void sendJsonResponse(HttpServletResponse response, String json) throws IOException {
        response.setContentType("application/json;");
        response.getWriter().println(json);
        this.maxNumberOfComments = 0;
    }

//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        String maxNumOfCommentStr = CommentUtils.getParameter(
//                request, /*name=*/Constants.MAXNUMBER, /*defaultValue=*/"10");
//
//        try {
//            this.maxNumberOfComments = Integer.parseInt(maxNumOfCommentStr);
//
//            if (this.maxNumberOfComments < 1 || this.maxNumberOfComments > 10) {
//                response.setContentType("text/html;");
//                response.getWriter().println("Please enter an integer between 1 and 10.");
//            }
//        } catch (NumberFormatException e) {
//            System.err.println("Could not convert to int: " + maxNumOfCommentStr);
//            response.setContentType("text/html;");
//            response.getWriter().println("Please enter an integer between 1 and 10.");
//        }
//
//        response.sendRedirect("/index.html");
//    }
}
