package com.github.idnbso.snippetodo.controller;

import com.github.idnbso.snippetodo.SnippeToDoPlatformException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.github.idnbso.snippetodo.SnippeToDoLogger.LOGGER;
import static com.github.idnbso.snippetodo.controller.SnippeToDoControllerUtil
        .handleSnippeToDoPlatformException;

/**
 * HomeController to handle any request from the SnippeToDo home page.
 *
 * @see HttpServlet
 */
@WebServlet("/home/*")
public class HomeController extends HttpServlet
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
                          HttpServletResponse response)
            throws ServletException, IOException
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
                         HttpServletResponse response)
            throws ServletException, IOException
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
                        LOGGER.info("Connected to home, dispatching index.jsp.");
                        dispatcher = getServletContext().getRequestDispatcher("/index.jsp");
                        dispatcher.forward(request, response);
                        break;
                    }
                    default:
                    {
                        response.sendRedirect("/");
                    }
                }
            }
        }
        catch (ServletException | IOException e)
        {
            handleSnippeToDoPlatformException(response, new SnippeToDoPlatformException(
                    "Cannot connect to the home page.", e.getCause()));
        }
    }
}
