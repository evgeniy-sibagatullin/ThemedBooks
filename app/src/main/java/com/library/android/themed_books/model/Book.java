package com.library.android.themed_books.model;

public class Book {

    private final String title;
    private final String mainAuthor;
    private final String publishedDate;
    private final String pageCount;

    public Book(String title, String mainAuthor, String publishedDate, String pageCount) {
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

    public String getPageCount() {
        return pageCount;
    }
}
