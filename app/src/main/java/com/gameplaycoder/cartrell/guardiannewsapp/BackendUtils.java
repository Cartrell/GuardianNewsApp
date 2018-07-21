package com.gameplaycoder.cartrell.guardiannewsapp;

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
import java.nio.charset.Charset;
import java.util.ArrayList;

public final class BackendUtils {

  /////////////////////////////////////////////////////////////////////////////////////////
  //=======================================================================================
  // static / const
  //=======================================================================================
  /////////////////////////////////////////////////////////////////////////////////////////
  private static final String LOG_TAG = "BackendUtils";

  /////////////////////////////////////////////////////////////////////////////////////////
  //=======================================================================================
  // public
  //=======================================================================================
  /////////////////////////////////////////////////////////////////////////////////////////

  //=======================================================================================
  // FetchNewsArticles
  //=======================================================================================
  public static ArrayList<NewsArticle>FetchNewsArticles(String requestUrl) {
    URL url = createUrl(requestUrl);

    String jsonResponse = null;
    try {
      jsonResponse = makeHttpRequest(url);
    } catch (IOException exception) {
      Log.e(LOG_TAG, "FetchNewsArticles", exception);
    }

    return(extractNewsArticles(jsonResponse));
  }

  /////////////////////////////////////////////////////////////////////////////////////////
  //=======================================================================================
  // private
  //=======================================================================================
  /////////////////////////////////////////////////////////////////////////////////////////

  //=======================================================================================
  // ctor
  //=======================================================================================
  /**
   * Create a private constructor because objects of this type should never br created.
   */
  private BackendUtils() {
  }

  //=======================================================================================
  // createUrl
  //=======================================================================================
  private static URL createUrl(String stringUrl) {
    URL url = null;
    try {
      url = new URL(stringUrl);
    } catch (MalformedURLException exception) {
      Log.e(LOG_TAG, "createUrl", exception);
    }
    return(url);
  }

  //=======================================================================================
  // extractNewsArticles
  //=======================================================================================
  private static ArrayList<NewsArticle> extractNewsArticles(String jsonResponse) {
    ArrayList<NewsArticle> newsArticles = new ArrayList<>();

    if (jsonResponse == null) {
      return(newsArticles);
    }

    try {
      JSONObject jsonObject = new JSONObject(jsonResponse);
      JSONObject jResponse = jsonObject.getJSONObject("response");
      JSONArray jResults = jResponse.getJSONArray("results");
      for (int index = 0; index < jResults.length(); index++) {
        JSONObject jNewsArticle = jResults.getJSONObject(index);
        newsArticles.add(new NewsArticle(
          jNewsArticle.getString("sectionName"),
          jNewsArticle.getString("webTitle"),
          jNewsArticle.getString("webPublicationDate"),
          jNewsArticle.getString("webUrl"),
          parseAuthor(jNewsArticle)));
      }
    } catch (JSONException exception) {
      Log.e(LOG_TAG, "extractNewsArticles", exception);
    }

    // Return the list of news articles
    return(newsArticles);
  }

  //=======================================================================================
  // makeHttpRequest
  //=======================================================================================
  private static String makeHttpRequest(URL url) throws IOException {
    String jsonResponse = "";

    // If the URL is null, then return early.
    if (url == null) {
      return(jsonResponse);
    }

    HttpURLConnection urlConnection = null;
    InputStream inputStream = null;

    final int READ_TIMEOUT_MS = 10000;
    final int CONNECT_TIMEOUT_MS = 15000;
    final int RESPONSE_CODE_OK = 200;
    final String REQUEST_METHOD = "GET";

    try {
      urlConnection = (HttpURLConnection) url.openConnection();
      urlConnection.setReadTimeout(READ_TIMEOUT_MS);
      urlConnection.setConnectTimeout(CONNECT_TIMEOUT_MS);
      urlConnection.setRequestMethod(REQUEST_METHOD);
      urlConnection.connect();

      // If the request was successful (response code OK),
      // then read the input stream and parse the response.
      if (urlConnection.getResponseCode() == RESPONSE_CODE_OK) {
        inputStream = urlConnection.getInputStream();
        jsonResponse = readFromStream(inputStream);
      } else {
        Log.e(LOG_TAG, "makeHttpRequest. Warning: Response code: " + urlConnection.getResponseCode());
      }
    } catch (IOException exception) {
      Log.e(LOG_TAG, "makeHttpRequest", exception);
    } finally {
      if (urlConnection != null) {
        urlConnection.disconnect();
      }
      if (inputStream != null) {
        inputStream.close();
      }
    }

    return(jsonResponse);
  }

  //=======================================================================================
  // parseAuthor
  //=======================================================================================
  private static String parseAuthor(JSONObject jNewsArticle) {
    String author = "";
    try {
      JSONArray jTags = jNewsArticle.getJSONArray("tags");
      JSONObject jTag = (JSONObject)jTags.get(0);
      author = jTag.getString("webTitle");
    } catch (JSONException exception) {
      Log.e(LOG_TAG, "parseAuthor", exception);
    }

    return(author);
  }

  //=======================================================================================
  // readFromStream
  //=======================================================================================
  private static String readFromStream(InputStream inputStream) throws IOException {
    final String CHAR_SET = "UTF-8";
    StringBuilder output = new StringBuilder();
    if (inputStream != null) {
      InputStreamReader inputStreamReader = new InputStreamReader(inputStream,
        Charset.forName(CHAR_SET));
      BufferedReader reader = new BufferedReader(inputStreamReader);
      String line = reader.readLine();
      while (line != null) {
        output.append(line);
        line = reader.readLine();
      }
    }
    return(output.toString());
  }
}