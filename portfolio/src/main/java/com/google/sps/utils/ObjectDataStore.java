package com.google.sps.utils;

import java.util.List;

/** Interface for classes whose instances can be written to and restored from Datastore.
 * Classes implementing the ObjectDataStore interface must also have a non-null static field called CREATOR of a type
 * that implements the EntityConvertibleCreator interface. */
public interface ObjectDataStore<T> {
    /**
     * Puts the object into Datastore.
     * @param item Object needed to be stored.
     */
    public void store(T item);

    /**
     * Loads the object from Datastore.
     * @param limit Upper limit of objects in the list.
     * @return An list of objects.
     */
    public List<T> load(int limit);
}
