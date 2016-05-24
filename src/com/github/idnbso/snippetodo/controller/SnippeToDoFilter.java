package com.github.idnbso.snippetodo.controller;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by Idan on 23-5-16.
 */
@WebFilter("/*")
public class SnippeToDoFilter implements Filter
{
    public void destroy()
    {
    }

    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain)
            throws ServletException, IOException
    {
        HttpServletRequest req = (HttpServletRequest) request;
        String path = req.getRequestURI().substring(req.getContextPath().length());

        if (path.startsWith("/resources/"))
        {
            // Let the resources be loaded normally
            chain.doFilter(request, response);
        }
        else if (path.startsWith("/client/"))
        {
            request.getRequestDispatcher(path).forward(request, response);
        }
        else
        {
            // Delegate to the front controller.
            request.getRequestDispatcher("/home" + path).forward(request, response);
        }
    }

    public void init(FilterConfig config) throws ServletException
    {
    }
}
