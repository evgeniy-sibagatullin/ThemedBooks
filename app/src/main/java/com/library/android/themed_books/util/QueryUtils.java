package com.library.android.themed_books.util;

import android.content.Context;
import android.util.Log;

import com.library.android.themed_books.R;
import com.library.android.themed_books.model.Book;

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

public final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();
    private static final String TITLE = "title";
    private static final String AUTHORS = "authors";
    private static final String PUBLISHED_DATE = "publishedDate";
    private static final String PAGE_COUNT = "pageCount";
    private Context mContext;

    public QueryUtils(Context context) {
        mContext = context;
    }

    public ArrayList<Book> fetchBookData(String requestUrl) {
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        return extractBooks(jsonResponse);
    }

    private URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) urlConnection.disconnect();
            if (inputStream != null) inputStream.close();
        }

        return jsonResponse;
    }

    private String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private ArrayList<Book> extractBooks(String booksJSON) {
        ArrayList<Book> books = new ArrayList<>();

        if (booksJSON == null) return books;

        try {
            JSONObject baseJsonResponse = new JSONObject(booksJSON);
            JSONArray bookItemsArray = baseJsonResponse.getJSONArray("items");

            for (int i = 0; i < bookItemsArray.length(); i++) {
                try {
                    JSONObject currentBook = bookItemsArray.getJSONObject(i);
                    JSONObject volumeInfo = currentBook.getJSONObject("volumeInfo");

                    String title = volumeInfo.getString(TITLE);

                    JSONArray authors = volumeInfo.optJSONArray(AUTHORS);
                    String noAuthor = mContext.getString(R.string.no_author);
                    String mainAuthor = authors == null ? noAuthor : authors.optString(0, noAuthor);

                    String publishedDate = volumeInfo.optString(PUBLISHED_DATE,
                            mContext.getString(R.string.no_date));

                    String pageCount = volumeInfo.optString(PAGE_COUNT,
                            mContext.getString(R.string.no_page_count));

                    books.add(new Book(title, mainAuthor, publishedDate, pageCount));
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "Problem parsing the book JSON", e);
                }
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem getting the books JSON items", e);
        }

        return books;
    }
}
