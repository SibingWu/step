package com.google.sps.data;

import java.time.LocalDateTime;

/** Represents a comment with related details. */
public final class Comment {
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
}
