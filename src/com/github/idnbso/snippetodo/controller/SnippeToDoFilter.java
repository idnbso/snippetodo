package com.github.idnbso.snippetodo.controller;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * SnippeToDoFilter is the filter for the root site to filter paths to their correct controllers.
 */
@WebFilter("/*")
public class SnippeToDoFilter implements Filter
{
    /**
     * Called by the web container to indicate to a filter that it is being placed into service. The
     * servlet container calls the init method exactly once after instantiating the filter.
     *
     * @param config filterConfig configuration of the filter
     * @throws ServletException
     */
    @Override
    public void init(FilterConfig config) throws ServletException
    {
    }

    /**
     * Called by the web container to indicate to a filter that it is being taken out of service.
     * This method is only called once all threads within the filter's doFilter method have exited
     * or after a timeout period has passed. After the web container calls this method, it will not
     * call the doFilter method again on this instance of the filter.
     */
    @Override
    public void destroy()
    {
    }

    /**
     * The doFilter method of the Filter is called by the container each time a request/response
     * pair is passed through the chain due to a client request for a resource at the end of the
     * chain. The FilterChain passed in to this method allows the Filter to pass on the request and
     * response to the next entity in the chain.
     *
     * @param request  ServletRequest with the path to filter
     * @param response ServletResponse of the filter
     * @param chain    FilterChain to filter with the requested path
     * @throws ServletException
     * @throws IOException
     */
    @Override
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
