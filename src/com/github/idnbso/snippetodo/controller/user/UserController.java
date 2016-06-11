package com.github.idnbso.snippetodo.controller.user;

import com.github.idnbso.snippetodo.controller.user.facebook.FBConnection;
import com.github.idnbso.snippetodo.controller.user.facebook.FBGraph;
import com.github.idnbso.snippetodo.SnippeToDoPlatformException;
import com.github.idnbso.snippetodo.model.ISnippeToDoDAO;
import com.github.idnbso.snippetodo.model.data.user.SnippeToDoUserDAO;
import com.github.idnbso.snippetodo.model.data.user.User;
import com.github.idnbso.snippetodo.model.data.user.UserException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.*;

import static com.github.idnbso.snippetodo.SnippeToDoLogger.LOGGER;
import static com.github.idnbso.snippetodo.controller.SnippeToDoControllerUtil.*;

/**
 * UserController to handle any request from the client for operations on SnippeToDo users.
 *
 * @see HttpServlet
 * @see User
 */
@WebServlet("/user/*")
public class UserController extends HttpServlet
{
    private ISnippeToDoDAO<User> snippeToDoUsersDB;

    /**
     * Called by the servlet container to indicate to a servlet that the servlet is being placed
     * into service.
     *
     * @throws ServletException if an exception occurs that interrupts the servlet's normal
     *                          operation
     */
    @Override
    public void init() throws ServletException
    {
        try
        {
            snippeToDoUsersDB = SnippeToDoUserDAO.getInstance();
        }
        catch (SnippeToDoPlatformException e)
        {
            LOGGER.info("ERROR UserController.init: " + e.getMessage());
        }
    }

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

    /**
     * Log out a user from the current session.
     *
     * @param request the HttpServletRequest containing the data of the user to be logged out
     * @throws SnippeToDoPlatformException
     * @see HttpSession
     */
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
            throw new SnippeToDoPlatformException(message, e);
        }
    }

    /**
     * Log in a user with Facebook API to the SnippeToDo client.
     *
     * @param request the HttpServletRequest containing the data of a Facebook user to be logged in
     * @throws SnippeToDoPlatformException
     * @see FBConnection
     * @see FBGraph
     */
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
                if (!strUserId.matches("\\d+"))
                {
                    Throwable t = new Throwable(
                            "ERROR facebookLogin: There is a problem with the Facebook profile " +
                                    "data id value of the current user.");
                    throw new SnippeToDoPlatformException(null, t);
                }

                userId = Integer.parseInt(strUserId);
            }

            User user = snippeToDoUsersDB.get(userId);
            // Create the current Facebook user if he does not exist in the database of users
            if (user == null)
            {
                String firstName = fbProfileData.get("firstName");
                String lastName = fbProfileData.get("lastName");
                user = new User(userId, null, firstName, lastName, null);
                snippeToDoUsersDB.create(user);
            }
            request.getSession().setAttribute("user", user);
        }
        catch (UserException | SnippeToDoPlatformException e)
        {
            String message = "There was a problem logging in with Facebook. Try again.";
            throw new SnippeToDoPlatformException(message, e);
        }
    }

    /**
     * Get the email address of the last user which successfully logged in to the client.
     *
     * @param request the HttpServletRequest containing all of the stored cookies
     * @return the user's email address which is stored as a cookie
     */
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

    /**
     * Check in the session if there is currently a user logged in to the client, and if so than
     * retrieve his first name.
     *
     * @param request the HttpServletRequest containing the data of the current user in the session
     * @return the first name of the user which is logged in to the client
     */
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

    /**
     * Process the user registration data before saving it to the database.
     *
     * @param request the HttpServletRequest containing the data of user registration
     * @throws SnippeToDoPlatformException
     */
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
        catch (UserException | SnippeToDoPlatformException e)
        {
            String message = "There was a problem creating a new user.";
            throw new SnippeToDoPlatformException(message, e);
        }
    }

    /**
     * Logs in a user to the SnippeToDo client.
     *
     * @param request  the HttpServletRequest containing the data of the user logging in
     * @param response the HttpServletResponse containing the cookie with logged in user details
     * @throws SnippeToDoPlatformException
     */
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
            throw new SnippeToDoPlatformException(message, e);
        }
    }

    /**
     * Authenticate a user by a password.
     *
     * @param user     the user to be authenticated
     * @param password the password from the user's input for the authentication
     * @return is the user authenticated successfully
     */
    private boolean authenticateUser(User user, String password)
    {
        return user != null && user.getPassword().equals(password);
    }

    /**
     * Get a user by its email address sent from the client.
     *
     * @param request the HttpServletRequest containing the current session
     * @param email   the email address to match a user account with
     * @return user that is a match for the supplied email address
     * @throws SnippeToDoPlatformException
     */
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