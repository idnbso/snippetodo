package com.github.idnbso.snippetodo.model;

import java.util.List;

import com.github.idnbso.snippetodo.SnippeToDoPlatformException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.github.idnbso.snippetodo.model.data.item.Item;
import com.github.idnbso.snippetodo.model.data.user.User;

/**
 * The HibernateSnippeToDoDAO abstract class implements the Data Access Object design pattern, using
 * Hibernate 3.x with the MySQL database, for the use of CRUD operations regarding to do items and
 * registered users of the SnippeToDo application.
 *
 * @author Idan Busso
 * @author Shani Kahila
 * @see ISnippeToDoDAO
 * @see Item
 * @see User
 */
public abstract class HibernateSnippeToDoDAO<T> implements ISnippeToDoDAO<T>
{
    private Class<T> databaseClass;
    private SessionFactory sessionFactory;

    /**
     * HibernateSnippeToDoDAO protected constructor for inheriting classes to initialize the session
     * factory.
     *
     * @throws SnippeToDoPlatformException
     */
    protected HibernateSnippeToDoDAO() throws SnippeToDoPlatformException
    {
        try
        {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        }
        catch (HibernateException e)
        {
            throw new SnippeToDoPlatformException(
                    "Initial SessionFactory creation failed: " + e.getMessage(), e.getCause());
        }
    }

    /**
     * Create a new entity to be added to the database.
     *
     * @param entity The entity to be added to the database
     * @throws SnippeToDoPlatformException
     * @see ISnippeToDoDAO#create(T)
     */
    public void create(final T entity) throws SnippeToDoPlatformException
    {
        Session session = null;
        Transaction transaction = null;

        try
        {
            // creating a new session
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            // save the new item in the database items table
            session.save(entity);
            // commit the changes to the database
            transaction.commit();
            session.close();
        }
        catch (HibernateException e)
        {
            if (transaction != null)
            {
                session.getTransaction().rollback();
            }
            String message = "Failed to create a new " + databaseClass.getName() +
                    " in the database: " + e.getMessage();
            throw new SnippeToDoPlatformException(message, e.getCause());
        }
    }

    /**
     * Delete an existing entity from the database by its id number.
     *
     * @param id The id number of the entity to be deleted from the database
     * @throws SnippeToDoPlatformException
     * @see ISnippeToDoDAO#deleteById(int)
     */
    public void deleteById(final int id) throws SnippeToDoPlatformException
    {
        Transaction transaction = null;

        try
        {
            // creating a new session
            Session session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            // get the specific entity from the database that match the id
            @SuppressWarnings("unchecked")
            final T entity = (T) session.get(databaseClass, id);
            // delete the entity from the database
            session.delete(entity);
            // commit the changes to the database
            transaction.commit();
            session.close();
        }
        catch (HibernateException e)
        {
            if (transaction != null)
            {
                transaction.rollback();
            }
            String message = "Failed to delete by id number an existing " +
                    databaseClass.getName() + " from the database: " + e.getMessage();
            throw new SnippeToDoPlatformException(message, e.getCause());
        }
    }

    /**
     * Delete an existing entity from the database.
     *
     * @param entity The entity to be deleted from the database
     * @throws SnippeToDoPlatformException
     * @see ISnippeToDoDAO#delete(T)
     */
    public void delete(final T entity) throws SnippeToDoPlatformException
    {
        Transaction transaction = null;

        try
        {
            // creating a new session
            Session session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            // delete the entity from the database
            session.delete(entity);
            // commit the changes to the database
            transaction.commit();
            session.close();
        }
        catch (HibernateException e)
        {
            if (transaction != null)
            {
                transaction.rollback();
            }
            String message = "Failed to delete an existing " + databaseClass.getName() +
                    " from the database: " + e.getMessage();
            throw new SnippeToDoPlatformException(message, e.getCause());
        }
    }

    /**
     * Get an array of all of the entities from the database.
     *
     * @return the array of all of the entities from the database
     * @throws SnippeToDoPlatformException
     * @see ISnippeToDoDAO#getAll()
     */
    @SuppressWarnings("unchecked")
    public List<T> getAll() throws SnippeToDoPlatformException
    {
        Transaction transaction = null;
        List<T> entitiesList;
        try
        {
            // creating a new session
            Session session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            // create a query to get all of the entities from database
            String query = "from " + databaseClass.getName();
            entitiesList = session.createQuery(query).list();
            // commit the query to the database
            transaction.commit();
            session.close();
        }
        catch (HibernateException e)
        {
            if (transaction != null)
            {
                transaction.rollback();
            }
            String message = "Failed to get all entities of type " + databaseClass.getName() +
                    " from the database: " + e.getMessage();
            throw new SnippeToDoPlatformException(message, e.getCause());
        }

        return entitiesList;
    }

    /**
     * Get a specific entity reference from the database.
     *
     * @param id The id number of the entity to be retrieved from the database
     * @return the specific entity reference from the database
     * @throws SnippeToDoPlatformException
     * @see ISnippeToDoDAO#get(int)
     */
    @SuppressWarnings("unchecked")
    public T get(final int id) throws SnippeToDoPlatformException
    {
        Transaction transaction = null;
        T entity;

        try
        {
            // creating a new session
            Session session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            // get the specific entity from the database that match the id
            entity = (T) session.get(databaseClass, id);
            // commit the changes to the database
            transaction.commit();
            session.close();
        }
        catch (HibernateException e)
        {
            if (transaction != null)
            {
                transaction.rollback();
            }
            String message = "Failed to get an entity of type " + databaseClass.getName() +
                    " from the database: " + e.getMessage();
            throw new SnippeToDoPlatformException(message, e.getCause());
        }

        return entity;
    }

    /**
     * Update an existing entity from the database.
     *
     * @param entity The entity to be updated in the database
     * @return the updated entity reference from the database
     * @throws SnippeToDoPlatformException
     * @see ISnippeToDoDAO#update(T)
     */
    @SuppressWarnings("unchecked")
    public T update(final T entity) throws SnippeToDoPlatformException
    {
        Transaction transaction = null;
        T updatedEntity;

        try
        {
            // creating a new session for updating items
            Session session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            // merge the specific entity from the database that match the id
            updatedEntity = (T) session.merge(entity);
            // commit the changes to the database
            transaction.commit();
            session.close();
        }
        catch (HibernateException e)
        {
            if (transaction != null)
            {
                transaction.rollback();
            }
            String message = "Failed to update an entity of type " + databaseClass.getName() +
                    " from the database: " + e.getMessage();
            throw new SnippeToDoPlatformException(message, e);
        }

        return updatedEntity;
    }

    /**
     * Set the database class to be used when constructing this generic abstract class, for the
     * particular storing of the specified  class.
     *
     * @param databaseClass the database class to be used by hibernate
     */
    protected final void setDatabaseClass(final Class<T> databaseClass)
    {
        this.databaseClass = databaseClass;
    }
}
