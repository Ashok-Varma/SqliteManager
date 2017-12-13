package com.ashokvarma.sqlitemanager.sample.db.sqlite;

/**
 * Class description
 *
 * @author ashok
 * @version 1.0
 * @since 12/12/17
 */
public class SqliteTypeTest {
    private int id;
    private int number;
    private int numberNon;
    private float floatNumber;
    private float floatNumberNon;
    private String string;
    private String stringEmpty;
    private String stringNull;
    private byte[] blob;
    private byte[] blobEmpty;
    private byte[] blobNull;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumberNon() {
        return numberNon;
    }

    public void setNumberNon(int numberNon) {
        this.numberNon = numberNon;
    }

    public float getFloatNumber() {
        return floatNumber;
    }

    public void setFloatNumber(float floatNumber) {
        this.floatNumber = floatNumber;
    }

    public float getFloatNumberNon() {
        return floatNumberNon;
    }

    public void setFloatNumberNon(float floatNumberNon) {
        this.floatNumberNon = floatNumberNon;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public String getStringEmpty() {
        return stringEmpty;
    }

    public void setStringEmpty(String stringEmpty) {
        this.stringEmpty = stringEmpty;
    }

    public String getStringNull() {
        return stringNull;
    }

    public void setStringNull(String stringNull) {
        this.stringNull = stringNull;
    }

    public byte[] getBlob() {
        return blob;
    }

    public void setBlob(byte[] blob) {
        this.blob = blob;
    }

    public byte[] getBlobEmpty() {
        return blobEmpty;
    }

    public void setBlobEmpty(byte[] blobEmpty) {
        this.blobEmpty = blobEmpty;
    }

    public byte[] getBlobNull() {
        return blobNull;
    }

    public void setBlobNull(byte[] blobNull) {
        this.blobNull = blobNull;
    }
}
