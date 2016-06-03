package com.github.idnbso.snippetodo.controller;

import com.github.idnbso.snippetodo.SnippeToDoPlatformException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.github.idnbso.snippetodo.SnippeToDoLogger.LOGGER;

/**
 * Created by Idan on 3-6-16.
 */
public class SnippeToDoControllerUtil
{
    public static void writeJsonResponse(HttpServletResponse response, String jsonResponse)
    {
        try
        {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonResponse);
        }
        catch (IOException e)
        {
            LOGGER.info(e.getCause().toString());
        }
    }

    public static void writeTextResponse(HttpServletResponse response, String text)
    {
        try
        {
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(text);
        }
        catch (IOException e)
        {
            LOGGER.info(e.getCause().toString());
        }
    }

    public static void handleSnippeToDoPlatformException(HttpServletResponse response,
                                                           SnippeToDoPlatformException e)
    {
        LOGGER.info(e.getCause().toString());

        // create the error message object to contain the string error message
        JsonObject jsonErrorMessageObj = new JsonObject();
        jsonErrorMessageObj.addProperty("message", e.getMessage());

        // create the error object
        JsonObject jsonErrorObj = new JsonObject();
        jsonErrorObj.add("error", jsonErrorMessageObj);

        // send the error object back to the view
        String jsonResponse = new Gson().toJson(jsonErrorObj);
        writeJsonResponse(response, jsonResponse);
    }
}
