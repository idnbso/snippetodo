package com.github.idnbso.snippetodo.controller.user.facebook;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.*;

public class FBGraph
{
    private String accessToken;

    public FBGraph(String accessToken)
    {
        this.accessToken = accessToken;
    }

    public String getFBGraph()
    {
        String graph = null;
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
            System.out.println(graph);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException("ERROR in getting FB graph data. " + e);
        }
        return graph;
    }

    public Map<String, String> getGraphData(String fbGraph)
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
            e.printStackTrace();
            throw new RuntimeException("ERROR in parsing FB graph data. " + e);
        }
        return fbProfile;
    }
}
