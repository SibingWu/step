package com.google.sps.utils;

import com.google.appengine.api.datastore.*;
import com.google.appengine.repackaged.com.google.datastore.v1.Datastore;
import com.google.sps.data.Comment;

import java.util.ArrayList;
import java.util.List;

/** Provides the service to interact with Datastore to store or load comment. */
public class CommentDataStore {

    static class Constants {
        private final static String KIND = "Comment";
        private final static String TIMESTAMP = "timestamp";
    }

    public static final ObjectDataStore<Comment> COMMENT_OBJECT_DATA_STORE = new ObjectDataStore<Comment>() {
        private DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();

        /**
         * Puts the entity into Datastore.
         * @param comment Comment object needed to be stored.
         */
        @Override
        public void store(Comment comment) {
            this.datastoreService.put(comment.toEntity(Constants.KIND));
        }

        /**
         * Loads the entity from Datastore.
         * @return An Iterable.
         */
        @Override
        public List<Comment> load() {

            Query query = new Query(Constants.KIND).addSort(Constants.TIMESTAMP, Query.SortDirection.DESCENDING);

            DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
            PreparedQuery results = datastoreService.prepare(query);

            List<Comment> comments = new ArrayList<>();
            for (Entity entity: results.asIterable()) {
                Comment comment = Comment.CREATOR.fromEntity(entity);
                comments.add(comment);
            }

            return comments;
        }
    };
}
