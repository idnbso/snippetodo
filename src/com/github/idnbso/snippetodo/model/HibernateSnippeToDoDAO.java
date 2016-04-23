/**
 * 
 */
package com.github.idnbso.snippetodo.model;

import java.io.Serializable;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.github.idnbso.snippetodo.model.data.item.Item;
import com.github.idnbso.snippetodo.model.data.user.User;

/**
 * The HibernateSnippeToDoDAO class implements the Data Access Object design
 * pattern, using Hibernate 3 with the MySQL database, for the use of CRUD
 * operations regarding ToDo items and registered users of the SnippeToDo
 * application.
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
     * HibernateSnippeToDoDAO protected constructor for inheriting classes to
     * initialize the session factory.
     * 
     * @throws SnippeToDoPlatformException
     */
    protected HibernateSnippeToDoDAO() throws SnippeToDoPlatformException
    {
        try
        {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        }
        catch (HibernateException ex)
        {
            throw new SnippeToDoPlatformException(
                    "Initial SessionFactory creation failed: " + ex.getMessage(), ex);
        }
    }

    /**
     * Create a new entity to be added to the database.
     * 
     * @param entity The entity to be added to the database
     * @see ISnippeToDoDAO#create(T)
     * @throws SnippeToDoPlatformException
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
        }
        catch (HibernateException e)
        {
            if (transaction != null)
            {
                session.getTransaction().rollback();
            }
            StringBuilder message = new StringBuilder();
            message.append("Failed to create a new ");
            message.append(databaseClass.getName());
            message.append(" in the database: ");
            message.append(e.getMessage());
            throw new SnippeToDoPlatformException(message.toString(), e);
        }
        finally
        {
            session.close();
        }
    }

    /**
     * Delete an existing entity from the database by its id number.
     * 
     * @param id The id number of the entity to be deleted from the database
     * @see ISnippeToDoDAO#deleteById(int)
     * @throws SnippeToDoPlatformException
     */
    public void deleteById(final int id) throws SnippeToDoPlatformException
    {
        Session session = null;
        Transaction transaction = null;

        try
        {
            // creating a new session
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            // get the specific entity from the database that match the id
            @SuppressWarnings("unchecked")
            final T entity = (T) session.get(databaseClass, id);
            // delete the entity from the database
            session.delete(entity);
            // commit the changes to the database
            transaction.commit();
        }
        catch (HibernateException e)
        {
            if (transaction != null)
            {
                transaction.rollback();
            }
            StringBuilder message = new StringBuilder();
            message.append("Failed to delete an existing ");
            message.append(databaseClass.getName());
            message.append(" from the database: ");
            message.append(e.getMessage());
            throw new SnippeToDoPlatformException(message.toString(), e);
        }
        finally
        {
            session.close();
        }
    }

    /**
     * Delete an existing entity from the database.
     * 
     * @param entity The entity to be deleted from the database
     * @see ISnippeToDoDAO#delete(T)
     * @throws SnippeToDoPlatformException
     */
    public void delete(final T entity) throws SnippeToDoPlatformException
    {
        Session session = null;
        Transaction transaction = null;

        try
        {
            // creating a new session
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            // delete the entity from the database
            session.delete(entity);
            // commit the changes to the database
            transaction.commit();
        }
        catch (HibernateException e)
        {
            if (transaction != null)
            {
                transaction.rollback();
            }
            StringBuilder message = new StringBuilder();
            message.append("Failed to delete an existing ");
            message.append(databaseClass.getName());
            message.append(" from the database: ");
            message.append(e.getMessage());
            throw new SnippeToDoPlatformException(message.toString(), e);
        }
        finally
        {
            session.close();
        }
    }

    /**
     * Get an array of all of the entities from the database.
     * 
     * @see ISnippeToDoDAO#getAll()
     * @throws SnippeToDoPlatformException
     * @return the array of all of the entities from the database
     */
    @SuppressWarnings("unchecked")
    public List<T> getAll() throws SnippeToDoPlatformException
    {
        Session session = null;
        Transaction transaction = null;
        List<T> entitiesList = null;
        try
        {
            // creating a new session
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            // create a query to get all of the entities from database
            StringBuilder query = new StringBuilder();
            query.append("from ");
            query.append(databaseClass.getName());
            entitiesList = session.createQuery(query.toString()).list();
            // commit the query to the database
            transaction.commit();
        }
        catch (HibernateException e)
        {
            if (transaction != null)
            {
                transaction.rollback();
            }
            StringBuilder message = new StringBuilder();
            message.append("Failed to get all entities of type ");
            message.append(databaseClass.getName());
            message.append(" from the database: ");
            message.append(e.getMessage());
            throw new SnippeToDoPlatformException(message.toString(), e);
        }
        finally
        {
            session.close();
        }

        return entitiesList;
    }

    /**
     * Get a specific entity reference from the database.
     * 
     * @param id The id number of the entity to be retrieved from the database
     * @see ISnippeToDoDAO#get(int)
     * @throws SnippeToDoPlatformException
     * @return the specific entity reference from the database
     */
    @SuppressWarnings("unchecked")
    public T get(final int id) throws SnippeToDoPlatformException
    {
        Session session = null;
        Transaction transaction = null;
        T entity = null;

        try
        {
            // creating a new session
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            // get the specific entity from the database that match the id
            entity = (T) session.get(databaseClass, id);
            // commit the changes to the database
            transaction.commit();
        }
        catch (HibernateException e)
        {
            if (transaction != null)
            {
                transaction.rollback();
            }
            StringBuilder message = new StringBuilder();
            message.append("Failed to get an entity of type ");
            message.append(databaseClass.getName());
            message.append(" from the database: ");
            message.append(e.getMessage());
            throw new SnippeToDoPlatformException(message.toString(), e);
        }
        finally
        {
            session.close();
        }

        return entity;
    }

    /**
     * Update an existing entity from the database.
     * 
     * @param entity The entity to be updated in the database
     * @see ISnippeToDoDAO#update(T)
     * @throws SnippeToDoPlatformException
     * @return the updated entity reference from the database
     */
    @SuppressWarnings("unchecked")
    public T update(final T entity) throws SnippeToDoPlatformException
    {
        Session session = null;
        Transaction transaction = null;
        T updatedEntity = null;

        try
        {
            // creating a new session for updating items
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            // merge the specific entity from the database that match the id
            updatedEntity = (T) session.merge(entity);
            // commit the changes to the database
            transaction.commit();
        }
        catch (HibernateException e)
        {
            if (transaction != null)
            {
                transaction.rollback();
            }
            StringBuilder message = new StringBuilder();
            message.append("Failed to update an entity of type ");
            message.append(databaseClass.getName());
            message.append(" from the database: ");
            message.append(e.getMessage());
            throw new SnippeToDoPlatformException(message.toString(), e);
        }
        finally
        {
            session.close();
        }

        return updatedEntity;
    }

    /**
     * Set the database class to be used when constructing this generic abstract
     * class, for the particular storing of the specified  class.
     * 
     * @param databaseClass the database class to be used by hibernate
     */
    protected final void setDatabaseClass(final Class<T> databaseClass)
    {
        this.databaseClass = databaseClass;
    }

    /**
     * Get the current session created by the session factory.
     * 
     * @return the current session created by the session factory
     */
    protected final Session getCurrentSession()
    {
        return sessionFactory.getCurrentSession();
    }
}
