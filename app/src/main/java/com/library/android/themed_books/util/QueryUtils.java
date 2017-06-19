/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.library.android.themed_books.util;

import android.util.Log;

import com.library.android.themed_books.model.Book;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public final class QueryUtils {

    private static final String TITLE = "title";
    private static final String AUTHORS = "authors";
    private static final String PUBLISHED_DATE = "publishedDate";
    private static final String PAGE_COUNT = "pageCount";

    private QueryUtils() {
    }

    public static ArrayList<Book> extractBooks(InputStream is) {

        ArrayList<Book> books = new ArrayList<>();

        try {
            JSONObject baseJsonResponse = new JSONObject(getStringFromStream(is));
            JSONArray bookItemsArray = baseJsonResponse.getJSONArray("items");

            for (int i = 0; i < bookItemsArray.length(); i++) {
                JSONObject currentBook = bookItemsArray.getJSONObject(i);
                JSONObject volumeInfo = currentBook.getJSONObject("volumeInfo");
                String title = volumeInfo.getString(TITLE);
                String mainAuthor = volumeInfo.getJSONArray(AUTHORS).getString(0);
                String publishedDate = volumeInfo.getString(PUBLISHED_DATE);
                int pageCount = volumeInfo.getInt(PAGE_COUNT);
                books.add(new Book(title, mainAuthor, publishedDate, pageCount));
            }
        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the books JSON results", e);
        } catch (IOException e) {
            Log.e("QueryUtils", "Problem getting the test books JSON data", e);
        }

        return books;
    }

    private static String getStringFromStream(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }

        reader.close();
        return sb.toString();
    }
}
