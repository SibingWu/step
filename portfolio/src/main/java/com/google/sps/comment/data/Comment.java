package com.google.sps.comment.data;

import com.google.appengine.api.datastore.Entity;
import com.google.sps.datastore.EntityConvertible;
import com.google.sps.datastore.EntityConvertibleCreator;

/** Represents a comment with related details. */
public final class Comment implements EntityConvertible {
    private static final String PROPERTY_NAME_COMMENTER = "commenter";
    private static final String PROPERTY_NAME_EMAIL = "email";
    private static final String PROPERTY_NAME_CONTENT = "content";
    private static final String PROPERTY_NAME_TIMESTAMP = "timestamp";

    private final long id; // for json creation
    private final String commenter;
    private final String email;
    private final String content;
    private final long timestamp;

    public Comment(String commenter, String email, String content, long timestamp) {
        this.id = 0;
        this.commenter = commenter;
        this.email = email;
        this.content = content;
        this.timestamp = timestamp;
    }

    private Comment(long id, String commenter, String email, String content, long timestamp) {
        this.id = id;
        this.commenter = commenter;
        this.email = email;
        this.content = content;
        this.timestamp = timestamp;
    }

    @Override
    public Entity toEntity(String kind) {
        Entity commentEntity = new Entity(kind);
        commentEntity.setProperty(PROPERTY_NAME_COMMENTER, this.commenter);
        commentEntity.setProperty(PROPERTY_NAME_EMAIL, this.email);
        commentEntity.setProperty(PROPERTY_NAME_CONTENT, this.content);
        commentEntity.setProperty(PROPERTY_NAME_TIMESTAMP, this.timestamp);

        return commentEntity;
    }

    public static final EntityConvertibleCreator<Comment> CREATOR
            = entity -> {
                long id = entity.getKey().getId();
                String commenter = (String) entity.getProperty(PROPERTY_NAME_COMMENTER);
                String email = (String) entity.getProperty(PROPERTY_NAME_EMAIL);
                String content = (String) entity.getProperty(PROPERTY_NAME_CONTENT);
                long timestamp = (long) entity.getProperty(PROPERTY_NAME_TIMESTAMP);

                Comment comment = new Comment(id, commenter, email, content, timestamp);
                return comment;
            };
}
