package com.github.idnbso.snippetodo.controller;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Idan on 23-5-16.
 */
@WebFilter("/*")
public class SnippeToDoFilter implements Filter
{
    public void init(FilterConfig config) throws ServletException
    {
    }

    public void destroy()
    {
    }

    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain)
            throws ServletException, IOException
    {
        HttpServletRequest req = (HttpServletRequest) request;
        String path = req.getRequestURI().substring(req.getContextPath().length());

        if (path.startsWith("/resources/") || path.startsWith("/client/") ||
                path.startsWith("/user/"))
        {
            chain.doFilter(request, response);
        }
        else
        {
            // Delegate to the front controller.
            request.getRequestDispatcher("/home" + path).forward(request, response);
        }
    }
}
