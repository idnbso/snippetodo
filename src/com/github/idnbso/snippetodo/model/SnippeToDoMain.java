/**
 * 
 */
package com.github.idnbso.snippetodo.model;

import com.github.idnbso.snippetodo.model.data.user.*;

import java.util.List;

import com.github.idnbso.snippetodo.model.data.item.*;
import static org.junit.Assert.*;

/**
 * Test class for the SnippeToDo application to test all of the ISnippeToDoDAO
 * interface's operations.
 * 
 * @author Idan Busso
 * @author Shani Kahila
 * @see ISnippeToDoDAO
 * @see SnippeToDoUserDAO
 * @see SnippeToDoItemDAO
 */
public class SnippeToDoMain
{
    public static void main(String[] args)
    {
        try
        {
            // Get an instance of the specific data type class to be used
            ISnippeToDoDAO<User> snippeToDoUserDB = SnippeToDoUserDAO.getInstance();
            ISnippeToDoDAO<Item> snippeToDoItemDB = SnippeToDoItemDAO.getInstance();

            // initialize database objects to be tested
            User idan = new User(1, "Idan");
            Item itemFirst = new Item(1, 1, "test1");
            Item itemSecond = new Item(2, 1, "test2");

            // Test creating a new user in the database
            snippeToDoUserDB.create(idan);
            printObjects(snippeToDoUserDB.getAll());

            // Test creating new items in the database
            snippeToDoItemDB.create(itemFirst);
            snippeToDoItemDB.create(itemSecond);
            printObjects(snippeToDoItemDB.getAll());

            // Test updating an existing item in the database
            itemFirst.setDescription("test1 test3");
            snippeToDoItemDB.update(itemFirst);
            printObjects(snippeToDoItemDB.getAll());

            // Test deleting an existing item from the database
            snippeToDoItemDB.delete(itemSecond);
            printObjects(snippeToDoItemDB.getAll());
        }
        catch (SnippeToDoPlatformException e)
        {
            e.getMessage();
        }
    }

    public static void printObjects(List<?> objects)
    {
        for (Object object : objects)
        {
            System.out.println(object);
        }
    }
}
