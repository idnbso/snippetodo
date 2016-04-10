/**
 * 
 */
package com.github.idnbso.snippetodo.model.data.user;

import com.github.idnbso.snippetodo.model.*;

/**
 * SnippeToDoUserDAO class to provide CRUD operation for User objects to be done
 * by Hibernate with the database table of users.
 * 
 * @author Idan Busso
 * @author Shani Kahila
 */
public class SnippeToDoUserDAO extends HibernateSnippeToDoDAO<User> implements ISnippeToDoDAO<User>
{
    /**
     * the instance reference is set to null if there was no instantiation
     * beforehand.
     */
    private static SnippeToDoUserDAO snippeToDoUserInstance = null;

    /**
     * Get the single instance object SnippeToDoUserDAO class according to the
     * implementation of the Singleton design pattern.
     *
     * @return the single instance object of the SnippeToDoUserDAO class.
     * @throws SnippeToDoPlatformException
     */
    public static SnippeToDoUserDAO getInstance() throws SnippeToDoPlatformException
    {
        if (snippeToDoUserInstance == null)
        {
            snippeToDoUserInstance = new SnippeToDoUserDAO();
        }
        return snippeToDoUserInstance;
    }

    /**
     * The SnippeToDoUserDAO class is implementing the singleton design pattern,
     * hence consists of a private default constructor.
     * @see User
     */
    private SnippeToDoUserDAO() throws SnippeToDoPlatformException
    {
        super();
        setDatabaseClass(User.class);
    }

}
