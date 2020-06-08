package com.google.sps.servlets;

import com.google.appengine.api.datastore.*;
import com.google.gson.Gson;
import com.google.sps.data.Comment;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.List;

import static com.google.sps.utils.Constants.COMMENT_KEY;
import static com.google.sps.utils.Constants.COMMENT_MAXNUMBER;
import static com.google.sps.utils.Constants.COMMENT_TIMESTAMP;


/** Servlet that handles posting comment content. */
@WebServlet("/list-comment")
public class ListCommentServlet extends CommentServlet {
    private int maxNumberOfComments;

    @Override
    public void init() {
        this.maxNumberOfComments = 0; // default value
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Loads the comment from Datastore
        List<Comment> comments = getComments(request);

        // Converts into json form
        String json = convertToJsonUsingGson(comments);

        // Sends the JSON as the response
        sendJsonResponse(response, json);
    }

    /** Loads the comment from Datastore */
    private List<Comment> getComments(HttpServletRequest request) {
        Query query = new Query(COMMENT_KEY).addSort(COMMENT_TIMESTAMP, Query.SortDirection.DESCENDING);

        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        PreparedQuery results = datastoreService.prepare(query);

        List<Comment> comments = new ArrayList<>();
        for (Entity entity: results.asIterable()) {
            if (comments.size() >= this.maxNumberOfComments) {
                break;
            }

            // Loads comments from Datastore
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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String maxNumOfCommentStr = getParameter(request, /*name=*/COMMENT_MAXNUMBER, /*defaultValue=*/"10");

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
}
