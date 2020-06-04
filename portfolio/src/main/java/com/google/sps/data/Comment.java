package com.google.sps.data;

import java.time.LocalDateTime;

/** Represents a comment with related details. */
public final class Comment {
    private final String name;
    private final String comment;
    private final LocalDateTime time;

    public Comment(String name, String comment, LocalDateTime time) {
        this.name = name;
        this.comment = comment;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public String getComment() {
        return comment;
    }

    public LocalDateTime getTime() {
        return time;
    }
}
