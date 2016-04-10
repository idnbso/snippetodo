package com.github.idnbso.snippetodo.controller;

import com.github.idnbso.snippetodo.model.ISnippeToDoDAO;
import com.github.idnbso.snippetodo.model.SnippeToDoPlatformException;
import com.github.idnbso.snippetodo.model.data.item.Item;
import com.github.idnbso.snippetodo.model.data.item.SnippeToDoItemDAO;
import com.github.idnbso.snippetodo.model.data.user.SnippeToDoUserDAO;
import com.github.idnbso.snippetodo.model.data.user.User;
import com.google.gson.Gson;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 */
@WebServlet("/client/*")
public class SnippeToDoController extends HttpServlet
{
    private ISnippeToDoDAO<User> snippeToDoUserDB;
    private ISnippeToDoDAO<Item> snippeToDoItemDB;
    private List<Item> listTodoItems = new ArrayList<>();
    private StringBuilder listTodoItemsOrder = new StringBuilder("listTodoItem0");
    private int currentUserId = 1;

    @Override
    public void init() throws ServletException
    {
        try
        {
            snippeToDoUserDB = SnippeToDoUserDAO.getInstance();
            snippeToDoItemDB = SnippeToDoItemDAO.getInstance();

            // get all of the items from database and organize them according to lists
            List<Item> items = snippeToDoItemDB.getAll();
            for (Item item : items)
            {
                // TODO: make a switch
                //listTodoItems.add(item.positionIndex, item);
                listTodoItems.add(item);
                listTodoItemsOrder.append("|listItem");
                listTodoItemsOrder.append(item.getId());
            }
        }
        catch (SnippeToDoPlatformException e)
        {
            System.out.println(e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException
    {
        doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException
    {
        try
        {
            String path = request.getPathInfo();
            RequestDispatcher dispatcher = null;

            switch (path)
            {
                case "/":
                {
                    dispatcher = getServletContext().getRequestDispatcher("/client.jsp");
                    dispatcher.forward(request, response);
                    break;
                }
                case "/orderoflisttodo":
                {
                    writeTextResponse(response, listTodoItemsOrder.toString());
                    break;
                }
                case "/initlisttodo":
                {
                    /* TODO: return a json with property of the order of elements, and also array
                        of all items objects */

                    // setup the to-do list (view)
                    // create a new json with only the items to be updated if at all else do nothing
                    String jsonResponse = new Gson().toJson(listTodoItems);
                    writeJsonResponse(response, jsonResponse);
                    break;
                }
                case "/newitem":
                {
                    createNewItem(request, response);
                    break;
                }
                default:
            }
        }
        catch (IOException | SnippeToDoPlatformException e)
        {
            System.out.println(e.getMessage()); // TODO: replace with alert in the view
        }
    }

    private void writeJsonResponse(HttpServletResponse response, String jsonResponse)
            throws IOException
    {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse);
    }

    private void writeTextResponse(HttpServletResponse response, String text)
            throws IOException
    {
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(text);
    }

    private void createNewItem(HttpServletRequest request, HttpServletResponse response)
            throws SnippeToDoPlatformException, IOException
    {
        // data from the request (view)
        String title = request.getParameter("item-title");

        // data for the response (view)
        Item newItem = new Item(1, currentUserId, title);

        // data for the database (model)
        snippeToDoItemDB.create(newItem);
        String jsonResponse = new Gson().toJson(newItem);
        writeJsonResponse(response, jsonResponse);
    }
}