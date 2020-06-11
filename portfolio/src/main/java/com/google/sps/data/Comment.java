package com.google.sps.data;

import com.google.appengine.api.datastore.Entity;

/** Represents a comment with related details. */
public final class Comment implements EntityConvertible {
    private static final String KIND = "Comment";
    private static final String PROPERTY_NAME_COMMENTER = "commenter";
    private static final String PROPERTY_NAME_CONTENT = "content";
    private static final String PROPERTY_NAME_TIMESTAMP = "timestamp";

    private final long id; // for json creation
    private final String commenter;
    private final String content;
    private final long timestamp;

    public Comment(String commenter, String content, long timestamp) {
        this.id = 0;
        this.commenter = commenter;
        this.content = content;
        this.timestamp = timestamp;
    }

    public Comment(long id, String commenter, String content, long timestamp) {
        this.id = id;
        this.commenter = commenter;
        this.content = content;
        this.timestamp = timestamp;
    }

    @Override
    public Entity toEntity(String key) {
        Entity commentEntity = new Entity(KIND);
        commentEntity.setProperty(PROPERTY_NAME_COMMENTER, this.commenter);
        commentEntity.setProperty(PROPERTY_NAME_CONTENT, this.content);
        commentEntity.setProperty(PROPERTY_NAME_TIMESTAMP, this.timestamp);

        return commentEntity;
    }

    public static final EntityConvertibleCreator<Comment> CREATOR
            = entity -> {
                long id = entity.getKey().getId();
                String commenter = (String) entity.getProperty(PROPERTY_NAME_COMMENTER);
                String content = (String) entity.getProperty(PROPERTY_NAME_CONTENT);
                long timestamp = (long) entity.getProperty(PROPERTY_NAME_TIMESTAMP);

                Comment comment = new Comment(id, commenter, content, timestamp);
                return comment;
            };
}
