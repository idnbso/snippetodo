package com.github.idnbso.snippetodo.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Idan on 23-5-16.
 */
@WebServlet("/home/*")
public class MainController extends HttpServlet
{
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException
    {

    }

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
                        dispatcher = getServletContext().getRequestDispatcher("/index.jsp");
                        dispatcher.forward(request, response);
                        break;

                    }
                    default:
                }
            }
        }
        catch (Exception e)
        {
            // TODO: replace with message for an alert in the view
            System.out.println(e.getMessage());
        }
    }
}
