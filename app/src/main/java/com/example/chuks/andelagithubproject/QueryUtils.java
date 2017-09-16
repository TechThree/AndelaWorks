package com.example.chuks.andelagithubproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chuks on 9/16/2017.
 */

public class QueryUtils {

    /**
     * Tag for Log messages
     */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Create private constructor which makes it impossible
     * to subclass this class
     */
    private QueryUtils() {

    }

    /**
     * Method that returns data in the form of the
     * {@link User} object
     *
     * @param requestUrl
     * @return
     */
    public static List<User> getGithubData(String requestUrl) {

        //Create URL object
        URL url = createUrl(requestUrl);

        //Perform HTTP request to the URL and receive a JSON response
        String jsonResponse = null;

        try {
            jsonResponse = makeHTTPRequest(url);
        } catch (IOException exception) {
            Log.e(LOG_TAG, "Problem making HTTP request", exception);
        }

        //Extract relevant fields from the JSON response and create
        //a list of {@link User} objects

        List<User> userData = extractFeatureFromJson(jsonResponse);
        return userData;
    }


    /**
     * '
     * Method takes in a url in the form of a string object
     * and returns a URL object(while throwing a MalformedURLException)
     *
     * @param strURL
     * @return
     */
    public static URL createUrl(String strURL) {
        Log.i(LOG_TAG, "createUrl method has started..");
        URL url = null;

        try {
            url = new URL(strURL);
        } catch (MalformedURLException exception) {
            //Log message to lemme know what went down
            Log.e(LOG_TAG, "String URL couldn't be converted to a URL object", exception);
        }

        return url;
    }


    /**
     * Establish connection to the url and return a JSON response
     * in the form of a String object
     *
     * @param url
     * @return
     */
    public static String makeHTTPRequest(URL url) throws IOException {

        Log.i(LOG_TAG, "makeHTTPRequest method has started...");
        String jsonResponse = "";

        //if the url is null, then return early
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /** this bad boy is in milli seconds*/);
            urlConnection.setConnectTimeout(15000 /**also in milliseconds*/);
            urlConnection.connect();


           /*
            * If the connection response is good, a la urlConnection == 200,
            * then let's get the goodies from the input stream
            *
            */
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readInputStream(inputStream);
            } else {
               /* Notify me that the response code wasn't 200*/
                Log.e(LOG_TAG, "Error in response code " + urlConnection.getResponseCode());
            }

        } catch (IOException exception) {
            Log.e(LOG_TAG, "Error establishing URL connection", exception);
        } finally {


           /*
           Ensure that we close both the HttpURLConnection and close the InputStream objects
            */
            if (urlConnection != null) {
                urlConnection.disconnect();
            }

            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return jsonResponse;

    }

    /*
    Creating this helper method so we can get data from the input stream by passing it through the
    buffered reader.
     */
    public static String readInputStream(InputStream inputStream) {
        Log.i(LOG_TAG, "readInputStream method has started");

        StringBuilder output = new StringBuilder();

        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            try {
                String line = bufferedReader.readLine();
                while (line != null) {
                    output.append(line);
                    line = bufferedReader.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return output.toString();

    }

    /**
     * Method gets the JSON response and extracts the needed
     * fields by parsing through the string
     *
     * @param jsonResponse
     * @return
     */
    public static List<User> extractFeatureFromJson(String jsonResponse) {

        Log.i(LOG_TAG, "extractFeatureFromJson method has started");
        //if the JSON string is empty or null then return early
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }

        List<User> userList = new ArrayList();

        try {

            //Start from zee root of all things
            //we be passing in the jsonResponse string object to create
            // a JSONObject named baseJsonObject
            JSONObject baseJsonObject = new JSONObject(jsonResponse);

            //get the items-value by passing in the key "items"
            JSONArray itemsArray = baseJsonObject.getJSONArray("items");

            //increment over the array and add the names and profile url to
            //the created arraylist
            for (int i = 0; i < itemsArray.length(); i++) {
                //start extracting from the first JSONObject in the array
                JSONObject currentUser = itemsArray.getJSONObject(i);

                //Developer UserName
                String userName = currentUser.getString("login");

                //Developer Profile URL
                String profileURL = currentUser.getString("html_url");

                //TODO: Find out how to use the avatar_url
                String profileImage = currentUser.getString("avatar_url");

                Bitmap userImage = DownloadImage(profileImage);
                Log.i(LOG_TAG, "Image " + i + " downloaded");

                userList.add(new User(userName, profileURL, userImage));

            }

        } catch (JSONException exception) {
            //Man has got to know if there is a problem here
            Log.e(LOG_TAG, "Did not parse the JSONResponse Properly..", exception);

        }

        return userList;
    }

    public static Bitmap DownloadImage(String StringUrl) {
        Bitmap bitmap = null;
        InputStream inputStream;

        URL url = createUrl(StringUrl);

        try {
            inputStream = getImageInputStream(url);
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error getting input stream", e);
        }


        return bitmap;
    }

    private static InputStream getImageInputStream(URL url) throws IOException {

        HttpURLConnection urlConnection;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /** this bad boy is in milli seconds*/);
            urlConnection.setConnectTimeout(15000 /**also in milliseconds*/);
            urlConnection.connect();

           /*
            * If the connection response is good, a la urlConnection == 200,
            * then let's get the goodies from the input stream
            *
            */
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
            }

        } catch (IOException exception) {
            Log.e(LOG_TAG, "Error establishing URL connection for the inputstream", exception);

        }

        return inputStream;
    }

}
