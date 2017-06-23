package com.library.android.themed_books.activity;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ListView;
import android.widget.TextView;

import com.library.android.themed_books.R;
import com.library.android.themed_books.adapter.BookAdapter;
import com.library.android.themed_books.loader.BookLoader;
import com.library.android.themed_books.model.Book;

import java.util.ArrayList;
import java.util.List;

public class ThemedBooksActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Book>> {

    private static final int BOOK_LOADER_ID = 1;

    private BookAdapter mBookAdapter;
    private String mTheme;
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupToolbar();

        ListView bookListView = (ListView) findViewById(R.id.book_list);
        mBookAdapter = new BookAdapter(this, new ArrayList<Book>());
        bookListView.setAdapter(mBookAdapter);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        getLoaderManager().initLoader(BOOK_LOADER_ID, null, this);
        handleRestartLoader();
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
                mBookAdapter.clear();
                handleRestartLoader();
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
        return new BookLoader(this, mTheme);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        if (books != null && !books.isEmpty()) {
            mBookAdapter.clear();
            mBookAdapter.addAll(books);
        }

        if (isConnected()) {
            mEmptyStateTextView.setText(R.string.select_theme);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        mBookAdapter.clear();
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getTitle());
        setSupportActionBar(toolbar);
    }

    private void handleRestartLoader() {
        if (isConnected()) {
            mEmptyStateTextView.setText(R.string.loading);
            getLoaderManager().restartLoader(BOOK_LOADER_ID, null, ThemedBooksActivity.this);
        } else {
            mEmptyStateTextView.setText(R.string.no_connection);
        }
    }

    private boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
