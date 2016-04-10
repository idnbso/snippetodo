/**
 * 
 */
package com.github.idnbso.snippetodo.model.data.item;

import com.github.idnbso.snippetodo.model.*;

/**
 * SnippeToDoItemDAO class to provide CRUD operation for Item objects to be done
 * by Hibernate with the database table of items.
 * 
 * @author Idan Busso
 * @author Shani Kahila
 */
public class SnippeToDoItemDAO extends HibernateSnippeToDoDAO<Item> implements ISnippeToDoDAO<Item>
{
    /**
     * the instance reference is set to null if there was no instantiation
     * beforehand.
     */
    private static SnippeToDoItemDAO snippeToDoItemInstance = null;

    /**
     * Get the single instance object SnippeToDoItemDAO class according to the
     * implementation of the Singleton design pattern.
     *
     * @return the single instance object of the SnippeToDoItemDAO class.
     * @throws SnippeToDoPlatformException
     */
    public static SnippeToDoItemDAO getInstance() throws SnippeToDoPlatformException
    {
        if (snippeToDoItemInstance == null)
        {
            snippeToDoItemInstance = new SnippeToDoItemDAO();
        }
        return snippeToDoItemInstance;
    }

    /**
     * The SnippeToDoItemDAO class is implementing the singleton design pattern,
     * hence consists of a private default constructor.
     * 
     * @see Item
     */
    private SnippeToDoItemDAO() throws SnippeToDoPlatformException
    {
        super();
        setDatabaseClass(Item.class);
    }
}
