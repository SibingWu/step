package com.google.sps.data;

import com.google.appengine.api.datastore.Entity;

/** Represents a comment with related details. */
public final class Comment implements EntityConvertible {
    private final String commenter;
    private final String content;
    private final long timestamp;

    public Comment(String commenter, String content, long timestamp) {
        this.commenter = commenter;
        this.content = content;
        this.timestamp = timestamp;
    }

    static class PropertyName {
        private final static String KIND = "Comment";
        private final static String COMMENTER = "commenter";
        private final static String CONTENT = "content";
        private final static String TIMESTAMP = "timestamp";
    }

    @Override
    public Entity toEntity(String key) {
        Entity commentEntity = new Entity(PropertyName.KIND);
        commentEntity.setProperty(PropertyName.COMMENTER, this.commenter);
        commentEntity.setProperty(PropertyName.CONTENT, this.content);
        commentEntity.setProperty(PropertyName.TIMESTAMP, this.timestamp);

        return commentEntity;
    }

    public static final EntityConvertibleCreator<Comment> CREATOR
            = entity -> {
                String commenter = (String) entity.getProperty(PropertyName.COMMENTER);
                String content = (String) entity.getProperty(PropertyName.CONTENT);
                long timestamp = (long) entity.getProperty(PropertyName.TIMESTAMP);

                Comment comment = new Comment(commenter, content, timestamp);
                return comment;
            };
}
