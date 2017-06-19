package com.ashokvarma.sqlitemanager.sample.db.sqlite;

/**
 * Class description
 *
 * @author ashokvarma
 * @version 1.0
 * @see
 * @since 17 Jun 2017
 */
public class Contact {

    private int id;
    private String name;
    private String address;

    public Contact() {
    }

    public Contact(int id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public Contact(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getId() {

        return id;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }
}