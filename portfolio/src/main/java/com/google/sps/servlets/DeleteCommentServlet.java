package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.sps.utils.CommentDataStore;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that handles deleting comment content. */
@WebServlet("/delete-comment")
public class DeleteCommentServlet extends HttpServlet {

    private CommentDataStore commentDataStore;

    private static final String KIND = "Comment";
    // TODO: actually this constant is used in both Comment and DeleteCommentServlet,
    //  seems not proper to put it private static final, how i shall do it?

    @Override
    public void init() {
        this.commentDataStore = new CommentDataStore(DatastoreServiceFactory.getDatastoreService());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        long id = Long.parseLong(request.getParameter("id"));

        this.commentDataStore.delete(KIND, id);
    }
}
