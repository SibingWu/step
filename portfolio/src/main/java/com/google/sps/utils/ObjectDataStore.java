package com.google.sps.utils;

import com.google.appengine.repackaged.com.google.common.collect.ImmutableList;

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
     * @param limit Upper limit of number of objects.
     * @return An immutable list of objects.
     */
    public ImmutableList<T> load(int limit);

    /**
     * Deletes a specific object in Datastore.
     * @param kind Entity kind name.
     * @param id Target entity id.
     */
    public void delete(String kind, long id);
}
