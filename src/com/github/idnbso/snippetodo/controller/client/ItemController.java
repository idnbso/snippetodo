package com.github.idnbso.snippetodo.controller.client;

import com.github.idnbso.snippetodo.SnippeToDoPlatformException;
import com.github.idnbso.snippetodo.model.data.item.Item;
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
@WebServlet("/client/item/*")
public class ItemController extends ClientController
{
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
                        deleteItem(request, response);
                        break;
                    }
                    case "/update":
                    {
                        updateItem(request, response);
                        break;
                    }
                    case "/updateposition":
                    {
                        updateItemPosition(request, response);
                        break;
                    }
                    case "/updatelist":
                    {
                        updateList(request, response);
                        break;
                    }
                    default:
                }
            }
        }
        catch (IOException | SnippeToDoPlatformException e)
        {
            // TODO: replace with message for an alert in the view
            e.printStackTrace();
//            throw new RuntimeException("ERROR: run time errors", e.getCause());
        }
    }

    private void createNewItem(HttpServletRequest request, HttpServletResponse response)
            throws SnippeToDoPlatformException, IOException, RuntimeException
    {

        // data from the request (view)
        String title = request.getParameter("item-title");
        String body = request.getParameter("item-body");
        int positionIndex = Integer.parseInt(request.getParameter("positionIndex"));
        int currentLastItemId = Integer.parseInt(
                request.getSession().getAttribute("currentLastItemId").toString()) + 1;
        request.getSession().setAttribute("currentLastItemId", currentLastItemId);
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
        String title = request.getParameter("snpptd-client-edititem-title");
        String body = request.getParameter("snpptd-client-edititem-body");
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

    private void getItem(HttpServletRequest request, HttpServletResponse response)
            throws SnippeToDoPlatformException, IOException
    {
        int itemId = Integer.parseInt(request.getParameter("id"));
        Item item = snippeToDoItemsDB.get(itemId);
        String jsonResponse = new Gson().toJson(item);
        writeJsonResponse(response, jsonResponse);
    }

    private void updateList(HttpServletRequest request, HttpServletResponse response)
            throws SnippeToDoPlatformException, IOException
    {
        final String elementDataIdName = "snpptd-client-list-item";
        String jsonArray = request.getParameter("order");
        Type listType = new TypeToken<List<String>>()
        {
        }.getType();
        List<String> itemsNames = new Gson().fromJson(jsonArray, listType);

        for (int itemIndex = 1; itemIndex < itemsNames.size(); itemIndex++)
        {
            int itemId =
                    Integer.parseInt(
                            itemsNames.get(itemIndex).substring(elementDataIdName.length()));
            Item item = snippeToDoItemsDB.get(itemId);
            item.setPositionIndex(itemIndex);
            snippeToDoItemsDB.update(item);
        }
    }
}