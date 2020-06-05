package com.google.sps.data;

import com.google.appengine.api.datastore.Entity;

// TODO: add javadoc
public interface EntityConvertible {
    Entity toEntity(String key);
}
