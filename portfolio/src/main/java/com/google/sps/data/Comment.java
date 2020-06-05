package com.google.sps.data;

import com.google.appengine.api.datastore.Entity;

import static com.google.sps.utils.Constants.COMMENT_COMMENTER;
import static com.google.sps.utils.Constants.COMMENT_CONTENT;
import static com.google.sps.utils.Constants.COMMENT_KEY;
import static com.google.sps.utils.Constants.COMMENT_TIMESTAMP;

/** Represents a comment with related details. */
public final class Comment implements EntityConvertible {
    private final long id;
    private final String commenter;
    private final String content;
    private final long timestamp;

    public Comment(long id, String commenter, String content, long timestamp) {
        this.id = id;
        this.commenter = commenter;
        this.content = content;
        this.timestamp = timestamp;
    }

    @Override
    public Entity toEntity(String key) {
        Entity commentEntity = new Entity(COMMENT_KEY);
        commentEntity.setProperty(COMMENT_COMMENTER, this.commenter);
        commentEntity.setProperty(COMMENT_CONTENT, this.content);
        commentEntity.setProperty(COMMENT_TIMESTAMP, this.timestamp);

        return commentEntity;
    }

    public static final EntityCreation<Comment> CREATOR
            = entity -> {
                long id = entity.getKey().getId();
                String commenter = (String) entity.getProperty(COMMENT_COMMENTER);
                String content = (String) entity.getProperty(COMMENT_CONTENT);
                long timestamp = (long) entity.getProperty(COMMENT_TIMESTAMP);

                Comment comment = new Comment(id, commenter, content, timestamp);
                return comment;
            };
}
