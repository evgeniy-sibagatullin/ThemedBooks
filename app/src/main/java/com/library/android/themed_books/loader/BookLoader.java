package com.library.android.themed_books.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.library.android.themed_books.model.Book;
import com.library.android.themed_books.util.QueryUtils;

import java.util.List;

public class BookLoader extends AsyncTaskLoader<List<Book>> {

    private String mUrl;

    public BookLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Book> loadInBackground() {
        return mUrl == null ? null : QueryUtils.fetchBookData(mUrl);
    }
}