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
    private String serialNumber;
    private int pages;
    private String genre;
    private int ageMinLimit;
    private int ageMaxLimit;
    private int sequelId;
    private int prequelId;

    public SqliteBook(int id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
    }

    public SqliteBook setId(int id) {
        this.id = id;
        return this;
    }

    public int getId() {
        return id;
    }

    public SqliteBook setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public SqliteBook setAuthor(String author) {
        this.author = author;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public SqliteBook setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
        return this;
    }

    public int getPages() {
        return pages;
    }

    public SqliteBook setPages(int pages) {
        this.pages = pages;
        return this;
    }

    public String getGenre() {
        return genre;
    }

    public SqliteBook setGenre(String genre) {
        this.genre = genre;
        return this;
    }

    public int getAgeMinLimit() {
        return ageMinLimit;
    }

    public SqliteBook setAgeMinLimit(int ageMinLimit) {
        this.ageMinLimit = ageMinLimit;
        return this;
    }

    public int getAgeMaxLimit() {
        return ageMaxLimit;
    }

    public SqliteBook setAgeMaxLimit(int ageMaxLimit) {
        this.ageMaxLimit = ageMaxLimit;
        return this;
    }

    public int getSequelId() {
        return sequelId;
    }

    public SqliteBook setSequelId(int sequelId) {
        this.sequelId = sequelId;
        return this;
    }

    public int getPrequelId() {
        return prequelId;
    }

    public SqliteBook setPrequelId(int prequelId) {
        this.prequelId = prequelId;
        return this;
    }
}