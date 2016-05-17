package com.github.idnbso.snippetodo.controller;

import com.github.idnbso.snippetodo.model.ISnippeToDoDAO;
import com.github.idnbso.snippetodo.model.SnippeToDoPlatformException;
import com.github.idnbso.snippetodo.model.data.item.Item;
import com.github.idnbso.snippetodo.model.data.item.SnippeToDoItemDAO;
import com.github.idnbso.snippetodo.model.data.user.SnippeToDoUserDAO;
import com.github.idnbso.snippetodo.model.data.user.User;
import com.google.gson.Gson;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;
import java.lang.reflect.Type;

import com.google.gson.reflect.TypeToken;

/**
 * TODO
 */
@WebServlet("/client/*")
public class SnippeToDoController extends HttpServlet
{
    private ISnippeToDoDAO<User> snippeToDoUsersDB;
    private ISnippeToDoDAO<Item> snippeToDoItemsDB;

    private int currentLastItemId = 0; // make a session attribute
    private ArrayList<String> lists = new ArrayList<>();

    static final Comparator<Item> POSITION_INDEX_ORDER =
            (t1, t2) -> t1.getPositionIndex() - t2.getPositionIndex(); // ascending order

    @Override
    public void init() throws ServletException
    {
        try
        {
            snippeToDoUsersDB = SnippeToDoUserDAO.getInstance();
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
                    User user = (User) request.getSession().getAttribute("user");
                    if (user != null)
                    {
                        initLists(request, user);
                        dispatcher = getServletContext().getRequestDispatcher("/client.jsp");
                        dispatcher.forward(request, response);
                    }

                    break;
                }
                case "/initlisttodo":
                {
                    ArrayList<Item> listTodo =
                            (ArrayList<Item>) request.getSession().getAttribute("listTodo");
                    String jsonResponse = new Gson().toJson(listTodo);
                    writeJsonResponse(response, jsonResponse);
                    break;
                }
                case "/initlisttoday":
                {
                    ArrayList<Item> listToday =
                            (ArrayList<Item>) request.getSession().getAttribute("listToday");
                    String jsonResponse = new Gson().toJson(listToday);
                    writeJsonResponse(response, jsonResponse);
                    break;
                }
                case "/initlistdoing":
                {
                    ArrayList<Item> listDoing =
                            (ArrayList<Item>) request.getSession().getAttribute("listDoing");
                    String jsonResponse = new Gson().toJson(listDoing);
                    writeJsonResponse(response, jsonResponse);
                    break;
                }
                case "/initlistcheck":
                {
                    ArrayList<Item> listCheck =
                            (ArrayList<Item>) request.getSession().getAttribute("listCheck");
                    String jsonResponse = new Gson().toJson(listCheck);
                    writeJsonResponse(response, jsonResponse);
                    break;
                }
                case "/initlistdone":
                {
                    ArrayList<Item> listDone =
                            (ArrayList<Item>) request.getSession().getAttribute("listDone");
                    String jsonResponse = new Gson().toJson(listDone);
                    writeJsonResponse(response, jsonResponse);
                    break;
                }
                case "/newitem":
                {
                    createNewItem(request, response);
                    break;
                }
                case "/getitem":
                {
                    getItem(request, response);
                    break;
                }
                case "/deleteitem":
                {
                    deleteItem(request, response);
                    break;
                }
                case "/updateitem":
                {
                    updateItem(request, response);
                    break;
                }
                case "/updateitemposition":
                {
                    updateItemPosition(request, response);
                    break;
                }
                case "/updatelist":
                {
                    updateList(request, response);
                    break;
                }
                case "/newuser":
                {
                    createNewUser(request, response);
                    break;
                }
                case "/login":
                {
                    loginUser(request, response);
                    response.sendRedirect("/client/");

                    break;
                }
                case "/logout":
                {
                    HttpSession session = request.getSession();
                    session.removeAttribute("user");
                    session.invalidate();
                    break;
                }
                default:
            }
        }
        catch (IOException | SnippeToDoPlatformException e)
        {
            // TODO: replace with message for an alert in the view
            System.out.println(e.getMessage());
        }
    }

    private void initLists(HttpServletRequest request, User user) throws IOException
    {
                            /* TODO: return a json with property of the order of elements, and
                            also array
                        of all items objects */

        // setup the to-do list (view)
        // create a new json with only the items to be updated if at all else do nothing


        try
        {
            if (user != null)
            {
                // get all of the items from database and organize them according to lists
                List<Item> items = snippeToDoItemsDB.getAll();
                List<ArrayList<Item>> currentUserItemsLists = new ArrayList<>();

                for (int index = 0; index < lists.size(); index++)
                {
                    currentUserItemsLists.add(index, new ArrayList<>());
                }

                // init the arrays of the lists that will contain the items
                for (Item item : items)
                {
                    int id = item.getId();
                    currentLastItemId = currentLastItemId < id ? id : currentLastItemId;

                    if (item.getUserId() == user.getId())
                    {
                        int currentListId = item.getListId();
                        ArrayList<Item> currentList = currentUserItemsLists.get(currentListId - 1);
                        currentList.add(item);
                    }
                }

                for (int index = 0; index < currentUserItemsLists.size(); index++)
                {
                    ArrayList<Item> list = currentUserItemsLists.get(index);
                    Collections.sort(list, POSITION_INDEX_ORDER);
                    request.getSession().setAttribute(lists.get(index), list);
                }
            }

        }
        catch (SnippeToDoPlatformException e)
        {
            System.out.println(e.getMessage());
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
        String body = request.getParameter("item-body");
        int positionIndex = Integer.parseInt(request.getParameter("positionIndex"));
        currentLastItemId++;
        User currentUser = (User) request.getSession().getAttribute("user");

        // data for the response (view)
        Item newItem = new Item(currentLastItemId,
                                currentUser.getId(),
                                1,
                                title,
                                body,
                                positionIndex);

        // data for the database (model)
        snippeToDoItemsDB.create(newItem);
        String jsonResponse = new Gson().toJson(newItem);
        writeJsonResponse(response, jsonResponse);
    }

    private void deleteItem(HttpServletRequest request, HttpServletResponse response)
            throws SnippeToDoPlatformException, IOException
    {
        int itemId = Integer.parseInt(request.getParameter("id"));
        snippeToDoItemsDB.deleteById(itemId);
    }

    private void updateItem(HttpServletRequest request, HttpServletResponse response)
            throws SnippeToDoPlatformException, IOException
    {
        String title = request.getParameter("edit-item-title");
        String body = request.getParameter("edit-item-body");
        int itemId = Integer.parseInt(request.getParameter("id"));
        Item item = snippeToDoItemsDB.get(itemId);

        // update the existing item with new data
        item.setTitle(title);
        item.setBody(body);
        item = snippeToDoItemsDB.update(item);

        String jsonResponse = new Gson().toJson(item);
        writeJsonResponse(response, jsonResponse);
    }

    private void updateItemPosition(HttpServletRequest request, HttpServletResponse response)
            throws SnippeToDoPlatformException, IOException
    {
        int positionIndex = Integer.parseInt(request.getParameter("positionIndex"));
        int listId = Integer.parseInt(request.getParameter("listId"));
        int itemId = Integer.parseInt(request.getParameter("id"));
        Item item = snippeToDoItemsDB.get(itemId);

        // update the existing item with new data
        item.setPositionIndex(positionIndex);
        item.setListId(listId);
        item = snippeToDoItemsDB.update(item);

        String jsonResponse = new Gson().toJson(item);
        writeJsonResponse(response, jsonResponse);
    }

    private void updateList(HttpServletRequest request, HttpServletResponse response)
            throws SnippeToDoPlatformException, IOException
    {
        String jsonArray = request.getParameter("order");
        Type listType = new TypeToken<List<String>>()
        {
        }.getType();
        List<String> itemsNames = new Gson().fromJson(jsonArray, listType);

        for (int itemIndex = 1; itemIndex < itemsNames.size(); itemIndex++)
        {
            int itemId = Integer.parseInt(itemsNames.get(itemIndex).substring(8));
            Item item = snippeToDoItemsDB.get(itemId);
            item.setPositionIndex(itemIndex);
            snippeToDoItemsDB.update(item);
        }
    }

    private void getItem(HttpServletRequest request, HttpServletResponse response)
            throws SnippeToDoPlatformException, IOException
    {
        int itemId = Integer.parseInt(request.getParameter("id"));
        Item item = snippeToDoItemsDB.get(itemId);
        String jsonResponse = new Gson().toJson(item);
        writeJsonResponse(response, jsonResponse);
    }

    private void createNewUser(HttpServletRequest request, HttpServletResponse response)
            throws SnippeToDoPlatformException, IOException
    {
        // data from the request (view)
        String email = request.getParameter("signupEmail");
        String firstName = request.getParameter("signupFirstName");
        String lastName = request.getParameter("signupLastName");
        String password = request.getParameter("signupPassword");

        // validate user data is not already exist
        boolean isUserExist = getUserByEmail(email) != null;

        if (!isUserExist)
        {
            // data for the response (view)
            User newUser = new User(1, email, firstName, lastName, password);

            // data for the database (model)
            snippeToDoUsersDB.create(newUser);
        }
    }

    private void loginUser(HttpServletRequest request, HttpServletResponse response)
            throws SnippeToDoPlatformException, ServletException, IOException
    {
        String email = request.getParameter("loginInputEmail");
        String password = request.getParameter("loginInputPassword");
        User user = getUserByEmail(email);
        boolean isUserAuthenticated = authenticateUser(user, password);
        if (isUserAuthenticated)
        {
            request.getSession().setAttribute("user", user);
        }
        else
        {
            // TODO: throw new exception with error message for the view with the response
        }
    }

    private boolean authenticateUser(User user, String password)
            throws SnippeToDoPlatformException
    {
        return user != null && user.getPassword().equals(password);
    }

    private User getUserByEmail(String email) throws SnippeToDoPlatformException
    {
        List<User> allUsers = snippeToDoUsersDB.getAll();
        User user = null;
        for (User currentUser : allUsers)
        {
            if (currentUser.getEmail().equals(email))
            {
                user = currentUser;
                break;
            }
        }

        return user;
    }
}