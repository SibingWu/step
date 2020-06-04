package com.google.sps.data;

import java.time.LocalDateTime;

/** Represents a comment with related details. */
public final class Comment {
    private final String commenter;
    private final String content;
    private final LocalDateTime time;

    public Comment(String commenter, String content, LocalDateTime time) {
        this.commenter = commenter;
        this.content = content;
        this.time = time;
    }

    public String getCommenter() {
        return commenter;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getTime() {
        return time;
    }
}
