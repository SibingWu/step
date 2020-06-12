package com.google.sps.datastore;

import com.google.appengine.api.datastore.Entity;

/** Interface that converts an Entity in Datastore into a wanted Object type. */
public interface EntityConvertibleCreator<T> {
    T fromEntity(Entity entity);
}
