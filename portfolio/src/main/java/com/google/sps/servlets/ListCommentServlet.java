package com.google.sps.servlets;

import com.google.gson.Gson;
import com.google.sps.data.Comment;
import com.google.sps.utils.CommentDataStore;
import com.google.sps.utils.ServletUtils;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;


/** Servlet that handles posting comment content. */
@WebServlet("/list-comment")
public class ListCommentServlet extends HttpServlet {

    private static final String PARAM_NAME_QUANTITY = "quantity";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Gets the displayed comment limit
        int limit = getMaxNumberOfComments(request, response);

        // Loads the comment from Datastore
        List<Comment> comments = getComments(limit);

        // Converts into json form
        String json = convertToJsonUsingGson(comments);

        // Sends the JSON as the response
        sendJsonResponse(response, json);
    }

    private int getMaxNumberOfComments(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String maxNumOfCommentStr = ServletUtils.getParameter(
                request, /*name=*/PARAM_NAME_QUANTITY, /*defaultValue=*/"0");

        int limit = 0;

        try {
            limit = Integer.parseInt(maxNumOfCommentStr);
        } catch (NumberFormatException e) {
            System.err.println("Could not convert to int: " + maxNumOfCommentStr);

            return limit;
        }

        return limit;
    }

    /** Loads the comment from Datastore */
    private List<Comment> getComments(int limit) {
        // Loads comments from Datastore
        List<Comment> comments = CommentDataStore.COMMENT_OBJECT_DATA_STORE.load(limit);

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
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
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

        response.sendRedirect("/index.html");
    }
}
