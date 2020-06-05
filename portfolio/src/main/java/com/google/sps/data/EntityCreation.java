package com.google.sps.data;

import com.google.appengine.api.datastore.Entity;

// TODO: add javadoc
public interface EntityCreation<T> {
    T fromEntity(Entity entity);
}
