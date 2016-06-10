/**
 *
 */
package com.github.idnbso.snippetodo.model;

import com.github.idnbso.snippetodo.SnippeToDoPlatformException;

import java.util.List;

/**
 * ISnippeToDoDAO interface provides all of the CRUD operations to be used in the SnippeToDo
 * database by the users.
 *
 * @see SnippeToDoPlatformException
 */
public interface ISnippeToDoDAO<T>
{
    /**
     * Create a new entity to be added to the database.
     *
     * @param entity The entity to be added to the database
     * @throws SnippeToDoPlatformException
     * @see ISnippeToDoDAO#create(T)
     */
    void create(final T entity) throws SnippeToDoPlatformException;

    /**
     * Delete an existing entity from the database by its id number.
     *
     * @param id The id number of the entity to be deleted from the database
     * @throws SnippeToDoPlatformException
     * @see ISnippeToDoDAO#deleteById(int)
     */
    void deleteById(final int id) throws SnippeToDoPlatformException;

    /**
     * Delete an existing entity from the database.
     *
     * @param entity The entity to be deleted from the database
     * @throws SnippeToDoPlatformException
     * @see ISnippeToDoDAO#delete(T)
     */
    void delete(final T entity) throws SnippeToDoPlatformException;

    /**
     * Get an array of all of the entities from the database.
     *
     * @return the array of all of the entities from the database
     * @throws SnippeToDoPlatformException
     * @see ISnippeToDoDAO#getAll()
     */
    List<T> getAll() throws SnippeToDoPlatformException;

    /**
     * Get a specific entity reference from the database.
     *
     * @param id The id number of the entity to be retrieved from the database
     * @return the specific entity reference from the database
     * @throws SnippeToDoPlatformException
     * @see ISnippeToDoDAO#get(int)
     */
    T get(final int id) throws SnippeToDoPlatformException;

    /**
     * Update an existing entity from the database.
     *
     * @param entity The entity to be updated in the database
     * @return the updated entity reference from the database
     * @throws SnippeToDoPlatformException
     * @see ISnippeToDoDAO#update(T)
     */
    T update(final T entity) throws SnippeToDoPlatformException;
}
