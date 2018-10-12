package application.newsapp;

import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public final class QueryMethods {

    private QueryMethods() {
    }

    public static ArrayList<Article> makeHTTPRequest(String urlString) {
        String jsonResponse = "";
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpURLConnection httpURLConnection = null;
        try {
            if (url != null) {
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setReadTimeout(10000);
                httpURLConnection.setConnectTimeout(10000);
                httpURLConnection.connect();

                //Connection was successful
                if (httpURLConnection.getResponseCode() == 200) {
                    jsonResponse = handleInputStream(httpURLConnection.getInputStream());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }

        return getDataFromJSON(jsonResponse);
    }

    public static String handleInputStream(InputStream inputStream)  {
        if (inputStream == null) {
            return "";
        }

        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder result = new StringBuilder();

        try {
            String line = bufferedReader.readLine();
            while(line != null) {
                result.append(line);
                line = bufferedReader.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Log.d("TEST", "" + result);

        return result.toString();
    }

    public static ArrayList<Article> getDataFromJSON(String json) {
        ArrayList<Article> result = new ArrayList<>();

        if (TextUtils.isEmpty(json)) {
            return result;
        }

        try {
            JSONObject root = new JSONObject(json);
            JSONObject response = root.getJSONObject("response");
            JSONArray results = response.getJSONArray("results");

            JSONObject resultsElement;
            boolean hasDifferentBackground = false;
            for (int i = 0; i < results.length(); i++) {
                resultsElement = results.getJSONObject(i);
                result.add(new Article(resultsElement.getString("webTitle"), resultsElement.getString("sectionName"), resultsElement.getString("webPublicationDate"), resultsElement.getString("webUrl"), false, false));
                hasDifferentBackground = !hasDifferentBackground;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }
}
