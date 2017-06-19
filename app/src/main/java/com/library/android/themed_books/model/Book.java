package com.library.android.themed_books.model;

public class Book {

    private final String title;
    private final String mainAuthor;
    private final String publishedDate;
    private final int pageCount;

    public Book(String title, String mainAuthor, String publishedDate, int pageCount) {
        this.title = title;
        this.mainAuthor = mainAuthor;
        this.publishedDate = publishedDate;
        this.pageCount = pageCount;
    }

    public String getTitle() {
        return title;
    }

    public String getMainAuthor() {
        return mainAuthor;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public int getPageCount() {
        return pageCount;
    }
}
