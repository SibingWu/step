package com.google.sps.utils;

import com.google.appengine.api.datastore.*;
import com.google.appengine.repackaged.com.google.datastore.v1.Datastore;
import com.google.sps.data.Comment;

import java.util.ArrayList;
import java.util.List;

/** Provides the service to interact with Datastore to store or load comment. */
public class CommentDataStore implements ObjectDataStore<Comment> {

    private final static String KIND = "Comment";
    private final static String TIMESTAMP = "timestamp";

    private DatastoreService datastoreService;

    public CommentDataStore(DatastoreService datastoreService) {
        this.datastoreService = datastoreService;
    }

    /**
     * Puts the comments into Datastore.
     * @param comment Comment object needed to be stored.
     */
    @Override
    public void store(Comment comment) {
        this.datastoreService.put(comment.toEntity(KIND));
    }

    /**
     * Loads the comments from Datastore.
     * @return An Iterable.
     */
    @Override
    public List<Comment> load() {

        Query query = new Query(KIND).addSort(TIMESTAMP, Query.SortDirection.DESCENDING);

        PreparedQuery results = this.datastoreService.prepare(query);

        List<Comment> comments = new ArrayList<>();
        for (Entity entity: results.asIterable()) {
            Comment comment = Comment.CREATOR.fromEntity(entity);
            comments.add(comment);
        }

        return comments;
    }
}
