package com.github.idnbso.snippetodo.controller.user.facebook;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import com.github.idnbso.snippetodo.SnippeToDoPlatformException;
import com.google.gson.*;

/**
 * FBGraph class for the connection to Facebook Graph API.
 */
public class FBGraph
{
    private String accessToken;

    /**
     * FBGraph class constructor
     *
     * @param accessToken the access token to be used with the Facebook Graph API
     */
    public FBGraph(String accessToken)
    {
        this.accessToken = accessToken;
    }

    /**
     * Get the Facebook Graph API JSON response according to the supplied acces token of the user to
     * be logged in with facebook.
     *
     * @return the Facebook Graph API JSON response
     * @throws SnippeToDoPlatformException
     */
    public String getFBGraph() throws SnippeToDoPlatformException
    {
        String graph;
        try
        {
            String g = "https://graph.facebook.com/me?" + accessToken;
            URL url = new URL(g);
            URLConnection urlConnection = url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()));
            String inputLine;
            StringBuilder builder = new StringBuilder();
            while ((inputLine = bufferedReader.readLine()) != null)
            {
                builder.append(inputLine).append("\n");
            }
            bufferedReader.close();
            graph = builder.toString();
        }
        catch (IOException e)
        {
            Throwable t =
                    new Throwable("ERROR getFBGraph: unable to get FB graph data. " + e);
            throw new SnippeToDoPlatformException(null, t);
        }

        return graph;
    }

    /**
     * Get the data of the user attempting to log in from the Facebook Graph API JSON response.
     *
     * @param fbGraph the Facebook Graph API JSON response
     * @return the Facebook profile of the logged in user
     * @throws SnippeToDoPlatformException
     */
    public Map<String, String> getGraphData(String fbGraph) throws SnippeToDoPlatformException
    {
        Map<String, String> fbProfile = new HashMap<>();

        try
        {
            JsonObject json = new JsonParser().parse(fbGraph).getAsJsonObject();
            fbProfile.put("id", json.get("id").getAsString());
            String fullName = json.get("name").getAsString();
            String[] name = fullName.split(" ");
            fbProfile.put("firstName", name[0]);
            fbProfile.put("lastName", name[name.length - 1]);
        }
        catch (JsonSyntaxException e)
        {
            Throwable t = new Throwable(
                    "ERROR getFBGraph: unable to parse FB graph data. " + e);
            throw new SnippeToDoPlatformException(null, t);
        }

        return fbProfile;
    }
}
