/**
 *
 */
package com.github.idnbso.snippetodo.model;

import java.util.List;

/**
 * ISnippeToDoDAO interface provides all of the CRUD operations to be used in the SnippeToDo
 * database by the users.
 *
 * @author Idan Busso
 * @author Shani Kahila
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
    public void create(final T entity) throws SnippeToDoPlatformException;

    /**
     * Delete an existing entity from the database by its id number.
     *
     * @param id The id number of the entity to be deleted from the database
     * @throws SnippeToDoPlatformException
     * @see ISnippeToDoDAO#deleteById(int)
     */
    public void deleteById(final int id) throws SnippeToDoPlatformException;

    /**
     * Delete an existing entity from the database.
     *
     * @param entity The entity to be deleted from the database
     * @throws SnippeToDoPlatformException
     * @see ISnippeToDoDAO#delete(T)
     */
    public void delete(final T entity) throws SnippeToDoPlatformException;

    /**
     * Get an array of all of the entities from the database.
     *
     * @return the array of all of the entities from the database
     * @throws SnippeToDoPlatformException
     * @see ISnippeToDoDAO#getAll()
     */
    public List<T> getAll() throws SnippeToDoPlatformException;

    /**
     * Get a specific entity reference from the database.
     *
     * @param id The id number of the entity to be retrieved from the database
     * @return the specific entity reference from the database
     * @throws SnippeToDoPlatformException
     * @see ISnippeToDoDAO#get(int)
     */
    public T get(final int id) throws SnippeToDoPlatformException;

    /**
     * Update an existing entity from the database.
     *
     * @param entity The entity to be updated in the database
     * @return the updated entity reference from the database
     * @throws SnippeToDoPlatformException
     * @see ISnippeToDoDAO#update(T)
     */
    public T update(final T entity) throws SnippeToDoPlatformException;
}
