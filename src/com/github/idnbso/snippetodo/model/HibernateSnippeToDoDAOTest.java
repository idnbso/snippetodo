package com.github.idnbso.snippetodo.model;

import static org.junit.Assert.*;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import com.github.idnbso.snippetodo.model.data.item.Item;
import com.github.idnbso.snippetodo.model.data.item.SnippeToDoItemDAO;
import com.github.idnbso.snippetodo.model.data.user.SnippeToDoUserDAO;
import com.github.idnbso.snippetodo.model.data.user.User;

/**
 * JUnit Test Case class for the HibernateSnippeToDoDAO abstract class which
 * implements the ISnippeTodoDAO interface that includes all of the database
 * methods to be tested.
 * 
 * @author Eli Margolin
 * @author Idan Busso
 *
 */
public class HibernateSnippeToDoDAOTest
{
    private ISnippeToDoDAO<User> snippeToDoUserDB;
    private ISnippeToDoDAO<Item> snippeToDoItemDB;
    private List<User> userList;
    private List<Item> itemList;
    private static SessionFactory sessionFactory;

    /**
     * @throws SnippeToDoPlatformException
     */
    @BeforeClass
    public static void setUpTest() throws SnippeToDoPlatformException
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
     * @throws SnippeToDoPlatformException
     */
    @Before
    public void setUp() throws SnippeToDoPlatformException
    {
        Session session = null;

        try
        {
            // get an instance of the specific data type class to be used
            snippeToDoUserDB = SnippeToDoUserDAO.getInstance();
            snippeToDoItemDB = SnippeToDoItemDAO.getInstance();

            // create lists for storing users and items
            userList = new ArrayList<User>();
            itemList = new ArrayList<Item>();
            // initialize database objects to be tested
            userList.add(new User(1, "Idan"));
            userList.add(new User(2, "Shani"));
            userList.add(new User(3, "Eli"));
            itemList.add(new Item(1, 1, "TestItem1"));
            itemList.add(new Item(2, 1, "TestItem2"));
            itemList.add(new Item(3, 2, "TestItem3"));
            itemList.add(new Item(4, 2, "TestItem4"));
            itemList.add(new Item(5, 3, "TestItem5"));
            itemList.add(new Item(6, 3, "TestItem6"));

            // The database tables users and items must be empty
            session = sessionFactory.openSession();
            session.createSQLQuery("TRUNCATE TABLE users").executeUpdate();
            session.createSQLQuery("TRUNCATE TABLE items").executeUpdate();
        }
        catch (HibernateException ex)
        {
            throw new SnippeToDoPlatformException("Table truncate failed: " + ex.getMessage(), ex);
        }
        finally
        {
            session.close();
        }
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws SnippeToDoPlatformException
    {
        for (User user : userList)
        {
            snippeToDoUserDB.delete(user);
            user = null;
            assertNull(user);
        }
        userList = null;

        for (Item item : itemList)
        {
            snippeToDoItemDB.delete(item);
            item = null;
            assertNull(item);
        }
        itemList = null;

        snippeToDoUserDB = null;
        assertNull(snippeToDoUserDB);
        snippeToDoItemDB = null;
        assertNull(snippeToDoItemDB);
    }

    /**
     * Test method for
     * {@link com.github.idnbso.snippetodo.model.HibernateSnippeToDoDAO#create(java.lang.Object)}
     * .
     * 
     * @throws SnippeToDoPlatformException
     */
    @Test
    public void testCreate() throws SnippeToDoPlatformException
    {
        // adding Idan Shani and Eli to the database
        for (User user : userList)
        {
            snippeToDoUserDB.create(user);
            snippeToDoUserDB.delete(user);
            snippeToDoUserDB.create(user);
        }

        List<User> expectedUserList = userList;
        // getting actual user objects from the database
        List<User> actualUserList = snippeToDoUserDB.getAll();
        // asserting users
        assertEquals(expectedUserList.size(), actualUserList.size());
        for (int i = 0; i < expectedUserList.size(); i++)
        {
            assertEquals(expectedUserList.get(i), actualUserList.get(i));
        }

        // adding items to the database
        for (Item item : itemList)
        {
            snippeToDoItemDB.create(item);
            snippeToDoItemDB.delete(item);
            snippeToDoItemDB.create(item);
        }

        List<Item> expectedItemList = itemList;
        // getting actual item objects from the database
        List<Item> actualItemList = snippeToDoItemDB.getAll();
        // asserting items
        assertEquals(expectedItemList.size(), actualItemList.size());
        for (int i = 0; i < expectedItemList.size(); i++)
        {
            assertEquals(expectedItemList.get(i), actualItemList.get(i));
        }
    }

    /**
     * Test method for
     * {@link com.github.idnbso.snippetodo.model.HibernateSnippeToDoDAO#deleteById(int)}
     * .
     * 
     * @throws SnippeToDoPlatformException
     */
    @Test
    public void testDeleteById() throws SnippeToDoPlatformException
    {
        // adding Idan Shani and Eli to the database
        for (User user : userList)
        {
            snippeToDoUserDB.create(user);
        }

        // deleting the last user from the database by id
        snippeToDoUserDB.deleteById(userList.get(userList.size() - 1).getId());
        snippeToDoUserDB.create(userList.get(userList.size() - 1));
        snippeToDoUserDB.deleteById(userList.get(userList.size() - 1).getId());
        // creating expected user list
        List<User> expectedUserList = userList;
        expectedUserList.remove(expectedUserList.size() - 1);
        // getting user objects from the database
        List<User> actualUserList = snippeToDoUserDB.getAll();
        // asserting users
        assertEquals(expectedUserList.size(), actualUserList.size());
        for (int i = 0; i < expectedUserList.size(); i++)
        {
            assertEquals(expectedUserList.get(i), actualUserList.get(i));
        }

        // adding items to the database
        for (Item item : itemList)
        {
            snippeToDoItemDB.create(item);
        }

        // deleting the last item from the database by id
        snippeToDoItemDB.deleteById(itemList.get(itemList.size() - 1).getId());
        snippeToDoItemDB.create(itemList.get(itemList.size() - 1));
        snippeToDoItemDB.deleteById(itemList.get(itemList.size() - 1).getId());
        // creating expected item list
        List<Item> expectedItemList = itemList;
        expectedItemList.remove(expectedItemList.size() - 1);
        // getting item objects from the database
        List<Item> actualItemList = snippeToDoItemDB.getAll();
        // asserting items
        assertEquals(expectedItemList.size(), actualItemList.size());
        for (int i = 0; i < expectedItemList.size(); i++)
        {
            assertEquals(expectedItemList.get(i), actualItemList.get(i));
        }
    }

    /**
     * Test method for
     * {@link com.github.idnbso.snippetodo.model.HibernateSnippeToDoDAO#delete(java.lang.Object)}
     * .
     * 
     * @throws SnippeToDoPlatformException
     */
    @Test
    public void testDelete() throws SnippeToDoPlatformException
    {
        // adding Idan Shani and Eli to the database
        for (User user : userList)
        {
            snippeToDoUserDB.create(user);
        }

        // deleting the last user from the database
        snippeToDoUserDB.delete(userList.get(userList.size() - 1));
        snippeToDoUserDB.create(userList.get(userList.size() - 1));
        snippeToDoUserDB.delete(userList.get(userList.size() - 1));
        // creating expected user list
        List<User> expectedUserList = userList;
        expectedUserList.remove(expectedUserList.size() - 1);
        // getting user objects from the database
        List<User> actualUserList = snippeToDoUserDB.getAll();
        // asserting users
        assertEquals(expectedUserList.size(), actualUserList.size());
        for (int i = 0; i < expectedUserList.size(); i++)
        {
            assertEquals(expectedUserList.get(i), actualUserList.get(i));
        }
        // adding items to the database
        for (Item item : itemList)
        {
            snippeToDoItemDB.create(item);
        }

        // deleting the last item from the database
        snippeToDoItemDB.delete(itemList.get(itemList.size() - 1));
        snippeToDoItemDB.create(itemList.get(itemList.size() - 1));
        snippeToDoItemDB.delete(itemList.get(itemList.size() - 1));
        // creating expected item list
        List<Item> expectedItemList = itemList;
        expectedItemList.remove(expectedItemList.size() - 1);
        // getting item objects from the database
        List<Item> actualItemList = snippeToDoItemDB.getAll();
        // asserting items
        assertEquals(expectedItemList.size(), actualItemList.size());
        for (int i = 0; i < expectedItemList.size(); i++)
        {
            assertEquals(expectedItemList.get(i), actualItemList.get(i));
        }
    }

    /**
     * Test method for
     * {@link com.github.idnbso.snippetodo.model.HibernateSnippeToDoDAO#getAll()}
     * .
     * 
     * @throws SnippeToDoPlatformException
     */
    @Test
    public void testGetAll() throws SnippeToDoPlatformException
    {
        // adding Idan Shani and Eli to the database
        for (User user : userList)
        {
            snippeToDoUserDB.create(user);
        }

        List<User> expectedUserList = userList;
        // getting actual user objects from the database
        List<User> actualUserList = snippeToDoUserDB.getAll();
        // asserting users
        assertEquals(expectedUserList.size(), actualUserList.size());
        for (int i = 0; i < expectedUserList.size(); i++)
        {
            assertEquals(expectedUserList.get(i), actualUserList.get(i));
        }

        // adding items to the database
        for (Item item : itemList)
        {
            snippeToDoItemDB.create(item);
        }

        List<Item> expectedItemList = itemList;
        // getting actual item objects from the database
        List<Item> actualItemList = snippeToDoItemDB.getAll();
        // asserting items
        assertEquals(expectedItemList.size(), actualItemList.size());
        for (int i = 0; i < expectedItemList.size(); i++)
        {
            assertEquals(expectedItemList.get(i), actualItemList.get(i));
        }
    }

    /**
     * Test method for
     * {@link com.github.idnbso.snippetodo.model.HibernateSnippeToDoDAO#get(int)}
     * .
     * 
     * @throws SnippeToDoPlatformException
     */
    @Test
    public void testGet() throws SnippeToDoPlatformException
    {
        // adding Idan Shani and Eli to the database
        for (User user : userList)
        {
            snippeToDoUserDB.create(user);
        }

        // setting expected first user
        User expectedFirstUser = userList.get(0);
        // setting actual first user
        User actualFirstUser = snippeToDoUserDB.get(1);
        // asserting users
        assertEquals(expectedFirstUser, actualFirstUser);
        // adding items to the database
        for (Item item : itemList)
        {
            snippeToDoItemDB.create(item);
        }

        // setting expected first item
        Item expectedFirstItem = itemList.get(0);
        // setting actual first item
        Item actualFirstItem = snippeToDoItemDB.get(1);
        // asserting items
        assertEquals(expectedFirstItem, actualFirstItem);
    }

    /**
     * Test method for
     * {@link com.github.idnbso.snippetodo.model.HibernateSnippeToDoDAO#update(java.lang.Object)}
     * .
     * 
     * @throws SnippeToDoPlatformException
     */
    @Test
    public void testUpdate() throws SnippeToDoPlatformException
    {
        // adding Idan Shani and Eli to the database
        for (User user : userList)
        {
            snippeToDoUserDB.create(user);
        }

        // updating first user name by adding family name
        User idan = snippeToDoUserDB.get(1);
        idan.setName("Idan Busso");
        snippeToDoUserDB.update(idan);
        // creating expected user name
        String expectedUserName = "Idan Busso";
        // getting actual user name from the database
        String actualUserName = snippeToDoUserDB.get(1).getName();
        // asserting user name strings
        assertEquals(expectedUserName, actualUserName);
        // adding items to the database
        for (Item item : itemList)
        {
            snippeToDoItemDB.create(item);
        }

        // updating first item's user id from 1 to 3
        Item firstItem = snippeToDoItemDB.get(1);
        firstItem.setUserId(3);
        snippeToDoItemDB.update(firstItem);
        // creating expected item's user id
        int expectedFirstItemUserId = firstItem.getUserId();
        // getting actal item's user id from the database
        int actualFirstItemUserId = snippeToDoItemDB.get(1).getUserId();
        // asserting item's user id
        assertEquals(expectedFirstItemUserId, actualFirstItemUserId);
    }
}
