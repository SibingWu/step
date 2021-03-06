package com.google.sps.comment.servlets;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.sps.comment.data.Comment;
import com.google.sps.comment.data.CommentDataStore;
import com.google.sps.utils.ServletUtils;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;

/** Servlet that handles getting list of comments. */
@WebServlet("/list-comment")
public final class ListCommentServlet extends HttpServlet {
    private static final String PARAM_NAME_QUANTITY = "quantity";
    private static final int DEFAULT_COMMENT_QUANTITY = 0;

    private CommentDataStore commentDataStore;

    @Override
    public void init() {
        this.commentDataStore = new CommentDataStore(DatastoreServiceFactory.getDatastoreService());
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Gets the displayed comment limit
        int limit = ServletUtils.getIntParameter(request, PARAM_NAME_QUANTITY, DEFAULT_COMMENT_QUANTITY);
        // TODO: parameter validation
        // TODO: error handling

        // Loads the comment from Datastore
        List<Comment> comments = this.commentDataStore.load(limit);

        // Converts into json form
        String json = ServletUtils.convertToJsonUsingGson(comments);

        // Sends the JSON as the response
        sendJsonResponse(response, json);
    }

    /** Sends the JSON as response */
    private void sendJsonResponse(HttpServletResponse response, String json) throws IOException {
        response.setContentType("application/json;");
        response.getWriter().println(json);
    }
}
