package com.github.idnbso.snippetodo.controller.user;

import com.github.idnbso.snippetodo.controller.user.facebook.FBConnection;
import com.github.idnbso.snippetodo.controller.user.facebook.FBGraph;
import com.github.idnbso.snippetodo.SnippeToDoPlatformException;
import com.github.idnbso.snippetodo.model.ISnippeToDoDAO;
import com.github.idnbso.snippetodo.model.data.user.SnippeToDoUserDAO;
import com.github.idnbso.snippetodo.model.data.user.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.*;

import static com.github.idnbso.snippetodo.controller.SnippeToDoControllerUtil
        .handleSnippeToDoPlatformException;
import static com.github.idnbso.snippetodo.controller.SnippeToDoControllerUtil.writeTextResponse;

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
            if (path != null)
            {
                switch (path)
                {
                    case "/new":
                    {
                        processUserRegistration(request);
                        break;
                    }
                    case "/checksession":
                    {
                        String firstName = checkUserSession(request);
                        writeTextResponse(response, firstName);
                        break;
                    }
                    case "/initlogin":
                    {
                        String userEmail = getEmailFromCookie(request);
                        writeTextResponse(response, userEmail);
                        break;
                    }
                    case "/login":
                    {
                        loginUser(request, response);
                        break;
                    }
                    case "/initfblogin":
                    {
                        FBConnection fbConnection = new FBConnection();
                        String redirectUrl = fbConnection.getFBAuthUrl();
                        writeTextResponse(response, redirectUrl);
                        break;
                    }
                    case "/facebooklogin":
                    {
                        // redirected here after facebook authentication
                        facebookLogin(request);
                        response.sendRedirect("/client/");
                        break;
                    }
                    case "/logout":
                    {
                        logoutUser(request);
                        break;
                    }
                    default:
                    {
                        response.sendRedirect("/");
                    }
                }
            }
        }
        catch (SnippeToDoPlatformException e)
        {
            handleSnippeToDoPlatformException(response, e);
        }
    }

    private void logoutUser(HttpServletRequest request) throws SnippeToDoPlatformException
    {
        try
        {
            HttpSession session = request.getSession();
            session.removeAttribute("user");
            session.invalidate();
        }
        catch (IllegalStateException e)
        {
            String exceptionMessage = e.getMessage();
            String message = exceptionMessage != null ? exceptionMessage :
                    "There was a problem logging out from the client.";
            throw new SnippeToDoPlatformException(message, e.getCause());
        }
    }

    private void facebookLogin(HttpServletRequest request) throws SnippeToDoPlatformException
    {
        try
        {
            String code = request.getParameter("code");
            if (code == null || code.equals(""))
            {
                throw new SnippeToDoPlatformException(null, new Throwable(
                        "ERROR facebookLogin: Didn't get code parameter in callback."));
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
        }
        catch (SnippeToDoPlatformException e)
        {
            String exceptionMessage = e.getMessage();
            String message = exceptionMessage != null ? exceptionMessage :
                    "There was a problem logging in with Facebook. Try again.";
            throw new SnippeToDoPlatformException(message, e.getCause());
        }
    }

    private String getEmailFromCookie(HttpServletRequest request)
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

        return userEmail;
    }

    private String checkUserSession(HttpServletRequest request)
    {
        String firstName = "";

        User user = (User) request.getSession().getAttribute("user");
        if (user != null)
        {
            firstName = user.getFirstName();
        }

        return firstName;
    }

    private void processUserRegistration(HttpServletRequest request)
            throws SnippeToDoPlatformException
    {
        try
        {
            // data from the request (view)
            String email = request.getParameter("email");
            String firstName = request.getParameter("first_name");
            String lastName = request.getParameter("last_name");
            String password = request.getParameter("password");

            if (email == null || password == null || firstName == null || lastName == null)
            {
                Throwable t = new Throwable(
                        "ERROR processUserRegistration: At least one value in the sign up form is" +
                                " of a null value.");
                throw new SnippeToDoPlatformException(
                        "There is a problem with one or more of the provided values.", t);
            }

            email = email.trim();
            password = password.trim();
            firstName = firstName.trim();
            lastName = lastName.trim();

            // verify whether user data already exist
            boolean isUserExist = getUserByEmail(request, email) != null;

            if (!isUserExist)
            {
                int currentLastUserId = Integer.parseInt(
                        request.getSession().getAttribute("currentLastUserId").toString());
                int newUserId = currentLastUserId + 1;

                // data for the response (view)
                User newUser = new User(newUserId, email, firstName, lastName, password);

                // data for the database (model)
                snippeToDoUsersDB.create(newUser);
            }
            else
            {
                Throwable t = new Throwable(
                        "ERROR processUserRegistration: There is a user with the same email " +
                                "address in the database.");
                throw new SnippeToDoPlatformException(
                        "There is already a registered user with the same email address.", t);
            }
        }
        catch (SnippeToDoPlatformException e)
        {
            String exceptionMessage = e.getMessage();
            String message = exceptionMessage != null ? exceptionMessage :
                    "There was a problem creating a new user.";
            throw new SnippeToDoPlatformException(message, e.getCause());
        }
    }

    private void loginUser(HttpServletRequest request, HttpServletResponse response)
            throws SnippeToDoPlatformException
    {
        try
        {
            String email = request.getParameter("email");
            String password = request.getParameter("password");

            if (email == null || password == null)
            {
                Throwable t = new Throwable(
                        "ERROR loginUser: At least one value in the login form is a null value.");
                throw new SnippeToDoPlatformException(
                        "There is a problem with one or more of the provided values.", t);
            }

            email = email.trim();
            password = password.trim();

            User user = getUserByEmail(request, email);
            if (user == null)
            {
                Throwable t = new Throwable(
                        "ERROR loginUser: There is no user with the input email address in the " +
                                "database.");
                throw new SnippeToDoPlatformException(
                        "There is no registered user with the input email address.", t);

            }

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
                Throwable t = new Throwable("ERROR loginUser: User authentication has failed.");
                throw new SnippeToDoPlatformException(
                        "The email and password values are incorrect.", t);
            }
        }
        catch (SnippeToDoPlatformException e)
        {
            String exceptionMessage = e.getMessage();
            String message = exceptionMessage != null ? exceptionMessage :
                    "There was a problem logging in to client. Try again.";
            throw new SnippeToDoPlatformException(message, e.getCause());
        }
    }

    private boolean authenticateUser(User user, String password)
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