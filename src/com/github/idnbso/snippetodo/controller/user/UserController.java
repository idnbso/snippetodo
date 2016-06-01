package com.github.idnbso.snippetodo.controller.user;

import com.github.idnbso.snippetodo.controller.user.facebook.FBConnection;
import com.github.idnbso.snippetodo.controller.user.facebook.FBGraph;
import com.github.idnbso.snippetodo.SnippeToDoPlatformException;
import com.github.idnbso.snippetodo.model.ISnippeToDoDAO;
import com.github.idnbso.snippetodo.model.data.item.SnippeToDoItemDAO;
import com.github.idnbso.snippetodo.model.data.user.SnippeToDoUserDAO;
import com.github.idnbso.snippetodo.model.data.user.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.*;

/**
 * TODO
 */
@WebServlet("/user/*")
public class UserController extends HttpServlet
{
    private ISnippeToDoDAO<User> snippeToDoUsersDB;

    @Override
    public void init() throws ServletException
    {
        try
        {
            snippeToDoUsersDB = SnippeToDoUserDAO.getInstance();
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
                        // data from the request (view)
                        String email =
                                request.getParameter("snpptd-home-signupinput-email");
                        String firstName =
                                request.getParameter("snpptd-home-signupinput-firstname");
                        String lastName =
                                request.getParameter("snpptd-home-signupinput-lastname");
                        String password =
                                request.getParameter("snpptd-home-signupinput-password");

                        createNewUser(request, email, firstName, lastName, password);
                        break;
                    }
                    case "/initlogin":
                    {
                        // get the current user's email from a saved cookie
                        Cookie[] cookies = request.getCookies();
                        String userEmail = "";
                        if (cookies != null)
                        {
                            for (Cookie cookie : cookies)
                            {
                                String cookieValue = cookie.getValue();
                                if (cookie.getName().equals("userEmail") &&
                                        cookieValue.length() > 0)
                                {
                                    userEmail = cookieValue;
                                }
                            }
                        }

                        response.setContentType("text/plain");
                        response.setCharacterEncoding("UTF-8");
                        response.getWriter().write(userEmail);
                        break;
                    }
                    case "/login":
                    {
                        loginUser(request, response);
                        break;
                    }
                    case "/facebooklogin":
                    {
                        String code = request.getParameter("code");
                        if (code == null || code.equals(""))
                        {
                            throw new RuntimeException(
                                    "ERROR: Didn't get code parameter in callback.");
                        }
                        FBConnection fbConnection = new FBConnection();
                        String accessToken = fbConnection.getAccessToken(code);

                        FBGraph fbGraph = new FBGraph(accessToken);
                        String graph = fbGraph.getFBGraph();
                        Map<String, String> fbProfileData = fbGraph.getGraphData(graph);
                        String strUserId = fbProfileData.get("id");

                        int userId = 0;

                        if (strUserId != null)
                        {
                            strUserId = strUserId.substring(0, 9);
                            // add try and catch for NumberFormatException
                            userId = Integer.parseInt(strUserId);
                        }

                        User user = snippeToDoUsersDB.get(userId);
                        if (user == null)
                        {
                            String firstName = fbProfileData.get("firstName");
                            String lastName = fbProfileData.get("lastName");
                            user = new User(userId, null, firstName, lastName, null);
                            snippeToDoUsersDB.create(user);
                        }
                        request.getSession().setAttribute("user", user);
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
        }
        catch (IOException | SnippeToDoPlatformException e)
        {
            // TODO: replace with message for an alert in the view
            e.printStackTrace();
            // throw new RuntimeException("ERROR: run time errors", e.getCause());
        }
    }

    private void createNewUser(HttpServletRequest request, String email, String firstName,
                               String lastName, String password)
            throws SnippeToDoPlatformException, IOException
    {
        // validate user data is not already exist
        boolean isUserExist = getUserByEmail(request, email) != null;

        if (!isUserExist)
        {
            int currentLastUserId =
                    Integer.parseInt(
                            request.getSession().getAttribute("currentLastUserId").toString());
            int newUserId = currentLastUserId + 1;
            // data for the response (view)
            User newUser = new User(newUserId, email, firstName, lastName, password);

            // data for the database (model)
            snippeToDoUsersDB.create(newUser);
        }
    }

    private void loginUser(HttpServletRequest request, HttpServletResponse response)
            throws SnippeToDoPlatformException, ServletException, IOException
    {
        String email = request.getParameter("snpptd-home-logininput-email");
        String password = request.getParameter("snpptd-home-logininput-password");
        if (email != null && password != null)
        {
            email = email.trim();
            password = password.trim();
            User user = getUserByEmail(request, email);
            boolean isUserAuthenticated = authenticateUser(user, password);
            if (isUserAuthenticated)
            {
                Cookie cookieUserEmail = new Cookie("userEmail", email);
                final int maxAgeOneDay = 86400;
                cookieUserEmail.setMaxAge(maxAgeOneDay);
                response.addCookie(cookieUserEmail);
                request.getSession().setAttribute("user", user);
            }
            else
            {
                // TODO: throw new exception with error message for the view with the response
            }
        }
    }

    private boolean authenticateUser(User user, String password)
            throws SnippeToDoPlatformException
    {
        return user != null && user.getPassword().equals(password);
    }

    private User getUserByEmail(HttpServletRequest request, String email)
            throws SnippeToDoPlatformException
    {
        List<User> allUsers = snippeToDoUsersDB.getAll();
        int currentLastUserId = 1;
        User user = null;
        for (User currentUser : allUsers)
        {
            String currentUserEmail = currentUser.getEmail();
            int id = currentUser.getId();
            if (currentUserEmail != null && currentLastUserId < id)
            {
                currentLastUserId = id;
            }
            if (currentUserEmail != null && currentUserEmail.equals(email))
            {
                user = currentUser;
                break;
            }
        }
        request.getSession().setAttribute("currentLastUserId", currentLastUserId);
        return user;
    }
}