package com.github.idnbso.snippetodo.controller.user.facebook;

import com.github.idnbso.snippetodo.SnippeToDoPlatformException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class FBConnection
{
    private static final String FB_APP_ID = "955639281222919";
    private static final String FB_APP_SECRET = "02040901b549c57fd003fc2f082257be";
    private static final String REDIRECT_URI =
            "http://snippetodo.azurewebsites.net/user/facebooklogin";

    private static String accessToken = "";

    public String getFBAuthUrl() throws SnippeToDoPlatformException
    {
        String fbLoginUrl;
        try
        {
            fbLoginUrl = "http://www.facebook.com/dialog/oauth?" + "client_id="
                    + FBConnection.FB_APP_ID + "&redirect_uri="
                    + URLEncoder.encode(FBConnection.REDIRECT_URI, "UTF-8")
                    + "&scope=email";
        }
        catch (UnsupportedEncodingException e)
        {
            throw new SnippeToDoPlatformException(null, e.getCause());
        }
        return fbLoginUrl;
    }

    public String getAccessToken(String code) throws SnippeToDoPlatformException
    {
        if ("".equals(accessToken))
        {
            URL fbGraphURL;
            try
            {
                fbGraphURL = new URL(getFBGraphUrl(code));
            }
            catch (SnippeToDoPlatformException e)
            {
                throw new SnippeToDoPlatformException(null, e.getCause());
            }
            catch (MalformedURLException e)
            {
                Throwable t = new Throwable(
                        "ERROR getAccessToken: Invalid code received: " + e.getCause());
                throw new SnippeToDoPlatformException(null, t);
            }

            URLConnection fbConnection;
            StringBuffer buffer;
            try
            {
                fbConnection = fbGraphURL.openConnection();
                BufferedReader in;
                in = new BufferedReader(new InputStreamReader(fbConnection.getInputStream()));
                String inputLine;
                buffer = new StringBuffer();
                while ((inputLine = in.readLine()) != null)
                {
                    buffer.append(inputLine).append("\n");
                }
                in.close();
            }
            catch (IOException e)
            {
                Throwable t =
                        new Throwable("ERROR getAccessToken: Unable to connect with Facebook: " +
                                              e.getCause());
                throw new SnippeToDoPlatformException(null, t);
            }

            accessToken = buffer.toString();
            if (accessToken.startsWith("{"))
            {
                Throwable t = new Throwable("ERROR getAccessToken: Access Token Invalid.");
                throw new SnippeToDoPlatformException(null, t);
            }
        }

        return accessToken;
    }

    private String getFBGraphUrl(String code) throws SnippeToDoPlatformException
    {
        String fbGraphUrl;
        try
        {
            fbGraphUrl = "https://graph.facebook.com/oauth/access_token?"
                    + "client_id=" + FBConnection.FB_APP_ID + "&redirect_uri="
                    + URLEncoder.encode(FBConnection.REDIRECT_URI, "UTF-8")
                    + "&client_secret=" + FB_APP_SECRET + "&code=" + code;
        }
        catch (UnsupportedEncodingException e)
        {
            throw new SnippeToDoPlatformException(null, e.getCause());
        }
        return fbGraphUrl;
    }
}
