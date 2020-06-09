package com.google.sps.data;

import com.google.appengine.api.datastore.Entity;

/** Interface that converts an Object to an Entity in Datastore. */
public interface EntityConvertible {
    Entity toEntity(String key);
}
