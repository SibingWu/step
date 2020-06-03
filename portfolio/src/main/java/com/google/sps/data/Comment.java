package com.google.sps.data;

import java.time.LocalDateTime;

public class Comment {
    private String name;
    private String comment;
    private LocalDateTime time;

    public Comment(String name, String comment, LocalDateTime time) {
        this.name = name;
        this.comment = comment;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
