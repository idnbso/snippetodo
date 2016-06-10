package com.github.idnbso.snippetodo.controller.client;

import com.github.idnbso.snippetodo.SnippeToDoPlatformException;
import com.github.idnbso.snippetodo.model.data.item.Item;
import com.github.idnbso.snippetodo.model.data.item.ItemException;
import com.github.idnbso.snippetodo.model.data.user.User;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.*;
import java.lang.reflect.Type;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import static com.github.idnbso.snippetodo.controller.SnippeToDoControllerUtil.*;

/**
 * ItemController to handle any request from the client for operations on SnippeToDo items. This
 * class extends ClientController to use the same database instance values.
 *
 * @see ClientController
 * @see Item
 */
@WebServlet("/client/item/*")
public class ItemController extends ClientController
{
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
    @SuppressWarnings("unchecked")
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        try
        {
            String path = request.getPathInfo();
            if (path != null)
            {
                switch (path)
                {
                    case "/new":
                    {
                        createNewItem(request, response);
                        break;
                    }
                    case "/get":
                    {
                        getItem(request, response);
                        break;
                    }
                    case "/delete":
                    {
                        deleteItem(request);
                        break;
                    }
                    case "/update":
                    {
                        updateItem(request, response);
                        break;
                    }
                    case "/updateposition":
                    {
                        updateItemPosition(request);
                        break;
                    }
                    case "/updatelist":
                    {
                        updateList(request);
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
            Throwable t = new Throwable(
                    "ERROR ItemController.doGet: Redirect to the client root path has failed.");
            handleSnippeToDoPlatformException(response, new SnippeToDoPlatformException(
                    "Failed to redirect to the client page.", t));
        }
        catch (SnippeToDoPlatformException e)
        {
            handleSnippeToDoPlatformException(response, e);
        }
    }

    /**
     * Create a new item in the database with values from the client.
     *
     * @param request  the HttpServletRequest containing the data of the item to be created
     * @param response the HttpServletResponse containing the created item in the database
     * @throws SnippeToDoPlatformException
     * @see Item
     */
    private void createNewItem(HttpServletRequest request, HttpServletResponse response)
            throws SnippeToDoPlatformException
    {
        try
        {
            // data from the request (view)
            String title = request.getParameter("item-title");
            String body = request.getParameter("item-body");
            String strPositionIndex = request.getParameter("positionIndex");
            String strCurrentLastItemId =
                    request.getSession().getAttribute("currentLastItemId").toString();

            if (title == null || body == null || strPositionIndex == null ||
                    strCurrentLastItemId == null || !strPositionIndex.matches("\\d+") ||
                    !strCurrentLastItemId.matches("\\d+"))
            {
                Throwable t = new Throwable(
                        "ERROR createNewItem: There is at least one value from the client which " +
                                "is invalid.");
                throw new SnippeToDoPlatformException(null, t);
            }
            int positionIndex = Integer.parseInt(strPositionIndex);
            int currentLastItemId = Integer.parseInt(strCurrentLastItemId) + 1;
            request.getSession().setAttribute("currentLastItemId", currentLastItemId);
            User currentUser = (User) request.getSession().getAttribute("user");

            // data for the response (view)
            final int listToDoId = 1;
            Item newItem = new Item(currentLastItemId, currentUser.getId(), listToDoId,
                                    title, body, positionIndex);

            // data for the database (model)
            snippeToDoItemsDB.create(newItem);
            String jsonResponse = new Gson().toJson(newItem);
            writeJsonResponse(response, jsonResponse);
        }
        catch (ItemException | SnippeToDoPlatformException e)
        {
            String message = "There was a problem creating a new item in the database.";
            throw new SnippeToDoPlatformException(message, e);
        }
    }

    /**
     * Delete an item from the database.
     *
     * @param request the HttpServletRequest containing the data of the item to be deleted
     * @throws SnippeToDoPlatformException
     */
    private void deleteItem(HttpServletRequest request)
            throws SnippeToDoPlatformException
    {
        try
        {
            String id = request.getParameter("id");

            if (id == null || !id.matches("\\d+"))
            {
                Throwable t = new Throwable(
                        "ERROR deleteItem: There is at least one value from the client which is " +
                                "invalid.");
                throw new SnippeToDoPlatformException(null, t);
            }
            int itemId = Integer.parseInt(id);
            snippeToDoItemsDB.deleteById(itemId);
        }
        catch (SnippeToDoPlatformException e)
        {
            String exceptionMessage = e.getMessage();
            String message = exceptionMessage != null ? exceptionMessage :
                    "There was a problem deleting an item in the database.";
            throw new SnippeToDoPlatformException(message, e);
        }
    }

    /**
     * Update an item in the database after an edit to its values has been made in the client
     *
     * @param request  the HttpServletRequest containing the data of the item to be updated
     * @param response the HttpServletResponse containing the updated item from the database
     * @throws SnippeToDoPlatformException
     */
    private void updateItem(HttpServletRequest request, HttpServletResponse response)
            throws SnippeToDoPlatformException
    {
        try
        {
            String title = request.getParameter("snpptd-client-edititem-title");
            String body = request.getParameter("snpptd-client-edititem-body");
            String id = request.getParameter("id");

            if (body == null || title == null || id == null || !id.matches("\\d+"))
            {
                Throwable t = new Throwable(
                        "ERROR updateItem: There is at least one value from the client which is " +
                                "invalid.");
                throw new SnippeToDoPlatformException(null, t);
            }
            int itemId = Integer.parseInt(id);
            Item item = snippeToDoItemsDB.get(itemId);

            // update the existing item with new data
            item.setTitle(title);
            item.setBody(body);
            item = snippeToDoItemsDB.update(item);

            String jsonResponse = new Gson().toJson(item);
            writeJsonResponse(response, jsonResponse);
        }
        catch (ItemException | SnippeToDoPlatformException e)
        {
            String message = "There was a problem updating the item's values in the database.";
            throw new SnippeToDoPlatformException(message, e);
        }
    }

    /**
     * Update the position of the item from the client in the database.
     *
     * @param request an HttpServletRequest with the parameters values of the item
     * @throws SnippeToDoPlatformException
     */
    private void updateItemPosition(HttpServletRequest request)
            throws SnippeToDoPlatformException
    {
        try
        {
            String paramPositionIndex = request.getParameter("positionIndex");
            String paramListId = request.getParameter("listId");
            String paramItemId = request.getParameter("id");

            if (paramPositionIndex == null || paramListId == null || paramItemId == null ||
                    !paramPositionIndex.matches("\\d+") || !paramListId.matches("\\d+") ||
                    !paramItemId.matches("\\d+"))
            {
                Throwable t = new Throwable(
                        "ERROR updateItemPosition: There is at least one value from the client " +
                                "which is invalid.");
                throw new SnippeToDoPlatformException(null, t);
            }

            int positionIndex = Integer.parseInt(paramPositionIndex);
            int listId = Integer.parseInt(paramListId);
            int itemId = Integer.parseInt(paramItemId);
            Item item = snippeToDoItemsDB.get(itemId);

            // update the existing item with new data
            item.setPositionIndex(positionIndex);
            item.setListId(listId);
            snippeToDoItemsDB.update(item);
        }
        catch (ItemException | SnippeToDoPlatformException e)
        {
            String message =
                    "There was a problem updating the item position value in the database.";
            throw new SnippeToDoPlatformException(message, e);
        }
    }

    /**
     * Get the item data from the database to be used in the client.
     *
     * @param request  the HttpServletRequest containing the data of the item to be retrieved
     * @param response the HttpServletResponse containing the retrieved item from the database
     * @throws SnippeToDoPlatformException
     */
    private void getItem(HttpServletRequest request, HttpServletResponse response)
            throws SnippeToDoPlatformException
    {
        try
        {
            String id = request.getParameter("id");

            if (id == null || !id.matches("\\d+"))
            {
                Throwable t = new Throwable(
                        "ERROR getItem: There is at least one value from the client which is " +
                                "invalid.");

                throw new SnippeToDoPlatformException(null, t);
            }
            int itemId = Integer.parseInt(id);
            Item item = snippeToDoItemsDB.get(itemId);
            String jsonResponse = new Gson().toJson(item);
            writeJsonResponse(response, jsonResponse);
        }
        catch (SnippeToDoPlatformException e)
        {
            String exceptionMessage = e.getMessage();
            String message = exceptionMessage != null ? exceptionMessage :
                    "There was a problem getting the item from the database.";
            throw new SnippeToDoPlatformException(message, e);
        }
    }

    /**
     * Update a list's items order in the database.
     *
     * @param request the HttpServletRequest containing the order of items of a list
     * @throws SnippeToDoPlatformException
     */
    private void updateList(HttpServletRequest request)
            throws SnippeToDoPlatformException
    {
        try
        {
            final String elementDataIdName = "snpptd-client-list-item";
            String jsonArray = request.getParameter("order");
            Type listType = new TypeToken<List<String>>()
            {
            }.getType();
            List<String> itemsNames = new Gson().fromJson(jsonArray, listType);

            for (int itemIndex = 1; itemIndex < itemsNames.size(); itemIndex++)
            {
                String strItemId = itemsNames.get(itemIndex).substring(elementDataIdName.length());
                if (!strItemId.matches("\\d+"))
                {
                    Throwable t = new Throwable(
                            "ERROR updateList: There is an item with invalid values from the " +
                                    "client.");
                    throw new SnippeToDoPlatformException(null, t);
                }

                int itemId = Integer.parseInt(strItemId);
                Item item = snippeToDoItemsDB.get(itemId);
                item.setPositionIndex(itemIndex);
                snippeToDoItemsDB.update(item);
            }
        }
        catch (JsonSyntaxException | JsonIOException | ItemException |
                SnippeToDoPlatformException e)
        {
            String message =
                    "There was a problem updating the list in the database with the new changes.";
            throw new SnippeToDoPlatformException(message, e);
        }
    }
}