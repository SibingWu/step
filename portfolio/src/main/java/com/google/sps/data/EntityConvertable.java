package com.google.sps.data;

import com.google.appengine.api.datastore.*;

import java.util.List;

// TODO: add javadoc
public interface EntityConvertable<T> {
    Entity toEntity(String key);
    List<T> fromEntity(String key);
}
