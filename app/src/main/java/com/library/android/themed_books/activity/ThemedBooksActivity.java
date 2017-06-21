package com.library.android.themed_books.activity;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ListView;

import com.library.android.themed_books.R;
import com.library.android.themed_books.adapter.BookAdapter;
import com.library.android.themed_books.loader.BookLoader;
import com.library.android.themed_books.model.Book;

import java.util.ArrayList;
import java.util.List;

public class ThemedBooksActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Book>> {

    private static final int BOOK_LOADER_ID = 1;
    public static final String BOOKS_API_REQUEST_URL_TEMPLATE =
            "https://www.googleapis.com/books/v1/volumes?q=%s&maxResults=10";

    private BookAdapter mBookAdapter;
    private String mTheme = "android";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupToolbar();

        ListView bookListView = (ListView) findViewById(R.id.book_list);
        mBookAdapter = new BookAdapter(this, new ArrayList<Book>());
        bookListView.setAdapter(mBookAdapter);

        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(BOOK_LOADER_ID, null, this);
    }

    protected void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getTitle());
        setSupportActionBar(toolbar);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.book_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mTheme = query;
                getLoaderManager().restartLoader(BOOK_LOADER_ID, null, ThemedBooksActivity.this);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

        return result;
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {
        return new BookLoader(this, String.format(BOOKS_API_REQUEST_URL_TEMPLATE, mTheme));
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        if (books != null && !books.isEmpty()) {
            mBookAdapter.clear();
            mBookAdapter.addAll(books);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        mBookAdapter.clear();
    }
}
