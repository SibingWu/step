package com.google.sps.utils;

import com.google.appengine.api.datastore.*;
import com.google.sps.data.Comment;
import com.google.sps.data.EntityConvertible;
import com.google.sps.data.EntityConvertibleCreator;

/** Provides the service to interact with Datastore to store or load comment. */
public class CommentDataStore {

    static class Constants {
        final static String KIND = "Comment";
        final static String TIMESTAMP = "timestamp";
    }

    /**
     * Puts the entity into Datastore.
     * @param comment Comment object needed to be stored.
     */
    public static void store(Comment comment) {
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        datastoreService.put(comment.toEntity(Constants.KIND));
    }

    /**
     * Loads the entity from Datastore.
     * @return An Iterable.
     */
    public static Iterable<Entity> load() {
        Query query = new Query(Constants.KIND).addSort(Constants.TIMESTAMP, Query.SortDirection.DESCENDING);

        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        PreparedQuery results = datastoreService.prepare(query);

        return results.asIterable();
    }
}
