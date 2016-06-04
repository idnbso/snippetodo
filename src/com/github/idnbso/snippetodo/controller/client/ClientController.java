package com.github.idnbso.snippetodo.controller.client;

import com.github.idnbso.snippetodo.model.ISnippeToDoDAO;
import com.github.idnbso.snippetodo.SnippeToDoPlatformException;
import com.github.idnbso.snippetodo.model.data.item.Item;
import com.github.idnbso.snippetodo.model.data.item.SnippeToDoItemDAO;
import com.github.idnbso.snippetodo.model.data.user.User;
import com.google.gson.Gson;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.*;

import static com.github.idnbso.snippetodo.controller.SnippeToDoControllerUtil.*;

/**
 * ClientController to handle any request from the SnippeToDo client.
 *
 * @see HttpServlet
 */
@WebServlet("/client/*")
public class ClientController extends HttpServlet
{
    ISnippeToDoDAO<Item> snippeToDoItemsDB;
    private ArrayList<String> lists = new ArrayList<>();
    private static final Comparator<Item> POSITION_INDEX_ORDER =
            (t1, t2) -> t1.getPositionIndex() - t2.getPositionIndex(); // ascending order

    /**
     * Called by the servlet container to indicate to a servlet that the servlet is being placed
     * into service.
     *
     * @throws ServletException if an exception occurs that interrupts the servlet's normal
     *                          operation
     */
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

    /**
     * Called by the server (via the service method) to allow a servlet to handle a POST request.
     *
     * @param request  an HttpServletRequest object that contains the request the client has made of
     *                 the servlet
     * @param response an HttpServletResponse object that contains the response the servlet sends to
     *                 the client
     * @throws ServletException if the request for the POST could not be handled
     * @throws IOException      if an input or output error is detected when the servlet handles the
     *                          request
     */
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException
    {
        doGet(request, response);
    }

    /**
     * Called by the server (via the service method) to allow a servlet to handle a GET request.
     *
     * @param request  an HttpServletRequest object that contains the request the client has made of
     *                 the servlet
     * @param response an HttpServletResponse object that contains the response the servlet sends to
     *                 the client
     * @throws ServletException if the request for the GET could not be handled
     * @throws IOException      if an input or output error is detected when the servlet handles the
     *                          GET request
     */
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
                        else
                        {
                            response.sendRedirect("/home");
                        }

                        break;
                    }
                    case "/initlist":
                    {
                        initList(request, response);
                        break;
                    }
                    default:
                    {
                        response.sendRedirect("/home");
                    }
                }
            }
        }
        catch (SnippeToDoPlatformException e)
        {
            handleSnippeToDoPlatformException(response, e);
        }
    }

    /**
     * Initialize the specific list at the client according to the request.
     *
     * @param request  the HttpServletRequest to access the session to get attributes
     * @param response the HttpServletResponse to return the list to the client
     * @throws SnippeToDoPlatformException
     */
    @SuppressWarnings("unchecked")
    private void initList(HttpServletRequest request, HttpServletResponse response)
            throws SnippeToDoPlatformException
    {
        try
        {
            String listName = request.getParameter("listName");
            if (listName == null)
            {
                Throwable t = new Throwable(
                        "ERROR initList: Failed to recieve the list name from the client.");
                throw new SnippeToDoPlatformException(null, t);
            }

            String sessionListName = "list" + listName;
            ArrayList<Item> list = (ArrayList<Item>) request.getSession()
                    .getAttribute(sessionListName);
            if (list == null)
            {
                Throwable t = new Throwable(
                        "ERROR initList: Failed to recieve a saved list from the session.");
                throw new SnippeToDoPlatformException(null, t);
            }

            String jsonResponse = new Gson().toJson(list);
            writeJsonResponse(response, jsonResponse);
        }
        catch (SnippeToDoPlatformException e)
        {
            String exceptionMessage = e.getMessage();
            String message = exceptionMessage != null ? exceptionMessage :
                    "There was a problem initializing the lists. Try again later.";
            throw new SnippeToDoPlatformException(message, e.getCause());
        }
    }

    /**
     * Initialize all lists at the view according to the request
     *
     * @param request the HttpServletRequest to access the session to save attributes
     * @param user    the current logged in user in the session
     * @throws SnippeToDoPlatformException
     */
    private void initLists(HttpServletRequest request, User user) throws SnippeToDoPlatformException
    {
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
            throw new SnippeToDoPlatformException(e.getMessage(), e.getCause());
        }
        catch (IndexOutOfBoundsException | IllegalStateException e)
        {
            throw new SnippeToDoPlatformException(
                    "There was a problem initialising the lists with data from the database.",
                    e.getCause());
        }
    }
}