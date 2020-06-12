package com.google.sps.comment.servlets;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.sps.comment.data.CommentDataStore;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that handles deleting comment content. */
@WebServlet("/delete-comment")
public final class DeleteCommentServlet extends HttpServlet {

    private CommentDataStore commentDataStore;

    @Override
    public void init() {
        this.commentDataStore = new CommentDataStore(DatastoreServiceFactory.getDatastoreService());
    }

    // TODO: try DELETE request and doDelete()
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // TODO: error handling and param validation.
        //  error handling: Id wrong format
        //  Id does not match any comment
        //  Id of comment not belong to you
        long id = Long.parseLong(request.getParameter("id"));

        this.commentDataStore.delete(id);
    }
}
