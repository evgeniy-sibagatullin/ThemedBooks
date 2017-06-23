package com.library.android.themed_books.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.library.android.themed_books.model.Book;
import com.library.android.themed_books.util.QueryUtils;

import java.util.ArrayList;
import java.util.List;

public class BookLoader extends AsyncTaskLoader<List<Book>> {

    private static final String BOOKS_API_REQUEST_URL_TEMPLATE =
            "https://www.googleapis.com/books/v1/volumes?q=%s&maxResults=10";

    private String mTheme;
    private Context mContext;

    public BookLoader(Context context, String theme) {
        super(context);
        mContext = context;
        mTheme = theme;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Book> loadInBackground() {
        List<Book> books;
        if (mTheme == null || mTheme.length() < 3) {
            books = new ArrayList<>();
        } else {
            QueryUtils queryUtils = new QueryUtils(mContext);
            books = queryUtils.fetchBookData(String.format(BOOKS_API_REQUEST_URL_TEMPLATE,
                    mTheme.replace(" ", ",")));
        }
        return books;
    }
}