package com.google.sps.utils;

import java.util.List;

/** Interface that provides the service to interact with Datastore to store or load object. */
public interface ObjectDataStore<T> {
    /**
     * Puts the entity into Datastore.
     * @param item Object needed to be stored.
     */
    public void store(T item);

    /**
     * Loads the entity from Datastore.
     * @return An list of objects.
     */
    public List<T> load();
}
