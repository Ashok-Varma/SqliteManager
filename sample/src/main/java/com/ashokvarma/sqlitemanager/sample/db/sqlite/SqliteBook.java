package com.ashokvarma.sqlitemanager.sample.db.sqlite;

/**
 * Class description
 *
 * @author ashokvarma
 * @version 1.0
 * @see
 * @since 17 Jun 2017
 */
public class SqliteBook {

    private int id;
    private String title;
    private String author;

    public SqliteBook() {
    }

    public SqliteBook(int id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
    }

    public SqliteBook(String title, String author) {
        this.title = title;
        this.author = author;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getId() {

        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }
}