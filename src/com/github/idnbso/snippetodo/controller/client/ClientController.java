package com.github.idnbso.snippetodo.controller.client;

import com.github.idnbso.snippetodo.controller.user.facebook.FBConnection;
import com.github.idnbso.snippetodo.controller.user.facebook.FBGraph;
import com.github.idnbso.snippetodo.model.ISnippeToDoDAO;
import com.github.idnbso.snippetodo.SnippeToDoPlatformException;
import com.github.idnbso.snippetodo.model.data.item.Item;
import com.github.idnbso.snippetodo.model.data.item.SnippeToDoItemDAO;
import com.github.idnbso.snippetodo.model.data.user.SnippeToDoUserDAO;
import com.github.idnbso.snippetodo.model.data.user.User;
import com.google.gson.Gson;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.*;
import java.lang.reflect.Type;

import com.google.gson.reflect.TypeToken;

/**
 * TODO
 */
@WebServlet("/client/*")
public class ClientController extends HttpServlet
{

    protected ISnippeToDoDAO<Item> snippeToDoItemsDB;
    protected ArrayList<String> lists = new ArrayList<>();
    protected static final Comparator<Item> POSITION_INDEX_ORDER =
            (t1, t2) -> t1.getPositionIndex() - t2.getPositionIndex(); // ascending order

    @Override
    public void init() throws ServletException
    {
        try
        {
            snippeToDoItemsDB = SnippeToDoItemDAO.getInstance();

            lists.add("listTodo");
            lists.add("listToday");
            lists.add("listDoing");
            lists.add("listCheck");
            lists.add("listDone");
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

    @SuppressWarnings("unchecked")
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException
    {
        try
        {
            String path = request.getPathInfo();
            RequestDispatcher dispatcher;
            if (path != null)
            {
                switch (path)
                {
                    case "/":
                    {
                        User user = (User) request.getSession().getAttribute("user");
                        if (user != null)
                        {
                            initLists(request, user);
                            dispatcher = getServletContext().getRequestDispatcher("/client.jsp");
                            dispatcher.forward(request, response);
                        }

                        break;
                    }
                    case "/initlist":
                    {
                        String listName = "list" + request.getParameter("listName");
                        ArrayList<Item> list =
                                (ArrayList<Item>) request.getSession().getAttribute(listName);
                        String jsonResponse = new Gson().toJson(list);
                        writeJsonResponse(response, jsonResponse);
                        break;
                    }
                    default:
                    {
                        response.sendRedirect("/");
                    }
                }
            }
        }
        catch (IOException e)
        {
            // TODO: replace with message for an alert in the view
            e.printStackTrace();
            // throw new RuntimeException("ERROR: run time errors", e.getCause());
        }
    }

    private void initLists(HttpServletRequest request, User user) throws IOException
    {
        // setup the to-do list (view)
        try
        {
            if (user != null)
            {
                // get all of the items from database and organize them according to lists
                List<Item> items = snippeToDoItemsDB.getAll();
                List<ArrayList<Item>> currentUserItemsLists = new ArrayList<>();
                int currentLastItemId = 0;

                for (int index = 0; index < lists.size(); index++)
                {
                    currentUserItemsLists.add(index, new ArrayList<>());
                }

                // init the arrays of the lists that will contain the items
                for (Item item : items)
                {
                    int id = item.getId();
                    if (currentLastItemId < id)
                    {
                        currentLastItemId = id;
                    }

                    if (item.getUserId() == user.getId())
                    {
                        int currentListId = item.getListId();
                        ArrayList<Item> currentList = currentUserItemsLists.get(currentListId - 1);
                        currentList.add(item);
                    }
                }

                // sort all of the current logged in user lists of items by their position index
                for (int index = 0; index < currentUserItemsLists.size(); index++)
                {
                    ArrayList<Item> list = currentUserItemsLists.get(index);
                    Collections.sort(list, POSITION_INDEX_ORDER);
                    request.getSession().setAttribute(lists.get(index), list);
                }

                request.getSession().setAttribute("currentLastItemId", currentLastItemId);
            }
        }
        catch (SnippeToDoPlatformException e)
        {
            System.out.println(e.getMessage());
        }
    }

    protected static void writeJsonResponse(HttpServletResponse response, String jsonResponse)
            throws IOException
    {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse);
    }
}