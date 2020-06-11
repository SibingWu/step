package com.google.sps.utils;

import com.google.appengine.api.datastore.*;
import com.google.appengine.repackaged.com.google.common.collect.ImmutableList;
import com.google.sps.data.Comment;

/** Provides the service to interact with Datastore to store or load comment. */
public final class CommentDataStore implements ObjectDataStore<Comment> {

    private static final String KIND = "Comment";
    private static final String TIMESTAMP = "timestamp";

    private final DatastoreService datastoreService;

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
     * @return An immutable list of comments.
     */
    @Override
    public ImmutableList<Comment> load(int limit) {
        Query query = new Query(KIND).addSort(TIMESTAMP, Query.SortDirection.DESCENDING);
        PreparedQuery results = this.datastoreService.prepare(query);
        Iterable<Entity> resultsIterable = results.asIterable(FetchOptions.Builder.withLimit(limit));

        ImmutableList.Builder<Comment> comments = ImmutableList.builder();
        for( Entity entity: resultsIterable){
            comments.add(Comment.CREATOR.fromEntity(entity));
        }
        return comments.build();
    }
}
