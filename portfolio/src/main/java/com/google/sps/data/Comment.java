package com.google.sps.data;

import com.google.appengine.api.datastore.Entity;

import java.time.LocalDateTime;
import java.util.List;

import static com.google.sps.utils.Constants.COMMENT_COMMENTER;
import static com.google.sps.utils.Constants.COMMENT_CONTENT;
import static com.google.sps.utils.Constants.COMMENT_KEY;
import static com.google.sps.utils.Constants.COMMENT_TIMESTAMP;

/** Represents a comment with related details. */
public final class Comment implements EntityConvertable {
    private final long id;
    private final String commenter;
    private final String content;
    private final LocalDateTime time;

    public Comment(long id, String commenter, String content, LocalDateTime time) {
        this.id = id;
        this.commenter = commenter;
        this.content = content;
        this.time = time;
    }

    @Override
    public Entity toEntity(String key) {
        long timestamp = System.currentTimeMillis();

        Entity commentEntity = new Entity(COMMENT_KEY);
        commentEntity.setProperty(COMMENT_COMMENTER, this.commenter);
        commentEntity.setProperty(COMMENT_CONTENT, this.content);
        commentEntity.setProperty(COMMENT_TIMESTAMP, timestamp);

        return commentEntity;
    }

    @Override
    public List<Comment> fromEntity(String key) {
        return null;
    }
}
