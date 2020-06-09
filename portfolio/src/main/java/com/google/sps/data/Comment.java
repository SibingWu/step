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

    static class Constants {
        final static String KIND = "Comment";
        final static String COMMENTER = "commenter";
        final static String CONTENT = "content";
        final static String TIMESTAMP = "timestamp";
    }

    @Override
    public Entity toEntity(String key) {
        Entity commentEntity = new Entity(Constants.KIND);
        commentEntity.setProperty(Constants.COMMENTER, this.commenter);
        commentEntity.setProperty(Constants.CONTENT, this.content);
        commentEntity.setProperty(Constants.TIMESTAMP, this.timestamp);

        return commentEntity;
    }

    public static final EntityConvertibleCreator<Comment> CREATOR
            = entity -> {
                String commenter = (String) entity.getProperty(Constants.COMMENTER);
                String content = (String) entity.getProperty(Constants.CONTENT);
                long timestamp = (long) entity.getProperty(Constants.TIMESTAMP);

                Comment comment = new Comment(commenter, content, timestamp);
                return comment;
            };
}
