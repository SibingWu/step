package com.google.sps.comment.data;

import com.google.appengine.api.datastore.*;
import com.google.appengine.repackaged.com.google.common.collect.ImmutableList;
import com.google.sps.comment.data.Comment;
import com.google.sps.datastore.ObjectDataStore;

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
     * @return Id of the entity.
     */
    @Override
    public long store(Comment comment) {
        Entity commentEntity = comment.toEntity(KIND);
        long id = commentEntity.getKey().getId();

        this.datastoreService.put(commentEntity);

        return id;
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

    /**
     * Deletes a specific comment in Datastore.
     * @param kind Comment entity kind name.
     * @param id Target comment entity id.
     */
    @Override
    public void delete(String kind, long id) {
        Key taskEntityKey = KeyFactory.createKey(kind, id);
        this.datastoreService.delete(taskEntityKey);
        // TODO: error handling: "java.lang.IllegalArgumentException - If the specified key was invalid."
    }
}
