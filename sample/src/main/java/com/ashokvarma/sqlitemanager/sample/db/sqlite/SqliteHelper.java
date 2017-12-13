package com.ashokvarma.sqlitemanager.sample.db.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Class description
 *
 * @author ashokvarma
 * @version 1.0
 * @see
 * @since 17 Jun 2017
 */
public class SqliteHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "TestDb";

    // Contacts table name
    private static final String TABLE_CONTACTS = "contacts";

    // Contacts Table Columns names
    private static final String CONTACT_KEY_ID = "id";
    private static final String CONTACT_KEY_NAME = "name";
    private static final String CONTACT_KEY_ADDR = "contact_address";

    // Contacts book name
    private static final String TABLE_BOOKS = "books";

    // Contacts Book Columns names
    private static final String BOOK_KEY_ID = "id";
    private static final String BOOK_KEY_TITLE = "title";
    private static final String BOOK_KEY_AUTHOR = "author";
    private static final String BOOK_KEY_SERIAL_NUMBER = "serial_number";
    private static final String BOOK_KEY_PAGES = "pages";
    private static final String BOOK_KEY_GENRE = "genre";
    private static final String BOOK_KEY_AGE_MIN_LIMIT = "age_min";
    private static final String BOOK_KEY_AGE_MAX_LIMIT = "age_max";
    private static final String BOOK_KEY_SEQUEL_ID = "sequel_id";
    private static final String BOOK_KEY_PREQUEL_ID = "prequel_id";

    // Contacts book name
    private static final String TABLE_TYPE_TEST = "type_test";

    // Contacts Book Columns names
    private static final String TYPE_TEST_KEY_ID = "id";
    private static final String TYPE_TEST_KEY_NUMBER = "number";
    private static final String TYPE_TEST_KEY_NUMBER_NON = "number_non";
    private static final String TYPE_TEST_KEY_FLOAT = "float";
    private static final String TYPE_TEST_KEY_FLOAT_NON = "float_non";
    private static final String TYPE_TEST_KEY_STRING = "string";
    private static final String TYPE_TEST_KEY_STRING_NULL = "string_null";
    private static final String TYPE_TEST_KEY_STRING_EMPTY = "string_empty";
    private static final String TYPE_TEST_KEY_BLOB = "blob";
    private static final String TYPE_TEST_KEY_BLOB_EMPTY = "blob_empty";
    private static final String TYPE_TEST_KEY_BLOB_NULL = "blob_null";

    public SqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        dropTables(db);
        // Creating tables again
        createTables(db);
    }

    public void clearTables() {
        SQLiteDatabase db = this.getWritableDatabase();
        dropTables(db);
        createTables(db);
    }

    public void dropTables(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TYPE_TEST);
    }

    public void createTables(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + CONTACT_KEY_ID + " INTEGER PRIMARY KEY,"
                + CONTACT_KEY_NAME + " TEXT,"
                + CONTACT_KEY_ADDR + " TEXT" + ")";
        String CREATE_BOOKS_TABLE = "CREATE TABLE " + TABLE_BOOKS + "("
                + BOOK_KEY_ID + " INTEGER PRIMARY KEY,"
                + BOOK_KEY_TITLE + " TEXT,"
                + BOOK_KEY_AUTHOR + " TEXT,"
                + BOOK_KEY_SERIAL_NUMBER + " TEXT,"
                + BOOK_KEY_PAGES + " INTEGER,"
                + BOOK_KEY_GENRE + " TEXT,"
                + BOOK_KEY_AGE_MIN_LIMIT + " INTEGER,"
                + BOOK_KEY_AGE_MAX_LIMIT + " INTEGER,"
                + BOOK_KEY_SEQUEL_ID + " INTEGER,"
                + BOOK_KEY_PREQUEL_ID + " INTEGER" + ")";
        String CREATE_TYPE_TEST_TABLE = "CREATE TABLE " + TABLE_TYPE_TEST + "("
                + TYPE_TEST_KEY_ID + " INTEGER PRIMARY KEY,"
                + TYPE_TEST_KEY_NUMBER + " INTEGER,"
                + TYPE_TEST_KEY_NUMBER_NON + " INTEGER,"
                + TYPE_TEST_KEY_FLOAT + " FLOAT,"
                + TYPE_TEST_KEY_FLOAT_NON + " FLOAT,"
                + TYPE_TEST_KEY_STRING + " TEXT,"
                + TYPE_TEST_KEY_STRING_NULL + " TEXT,"
                + TYPE_TEST_KEY_STRING_EMPTY + " TEXT,"
                + TYPE_TEST_KEY_BLOB + " BLOB,"
                + TYPE_TEST_KEY_BLOB_EMPTY + " BLOB,"
                + TYPE_TEST_KEY_BLOB_NULL + " BLOB" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
        db.execSQL(CREATE_BOOKS_TABLE);
        db.execSQL(CREATE_TYPE_TEST_TABLE);
        populateTestDataAsync();
    }

    public void populateTestDataAsync() {
        PopulateDataAsync task = new PopulateDataAsync(this);
        task.execute();
    }

    public void populateTestData() {
        int contactCount = 50;
        for (int i = 1; i <= contactCount; i++) {
            Contact contact = new Contact(i, i % 9 == 0 ? getTheBigText() : "Contact " + i, i % 3 == 0 ? "" : "Address " + (contactCount + 1 - i));
            addContact(contact);
        }

        int bookCount = 100;
        for (int i = 1; i <= bookCount; i++) {
            SqliteBook sqliteBook = new SqliteBook(i, i % 5 == 0 ? getTheBigText() : "Book " + i, i % 10 == 0 ? null : "Author " + (bookCount + 1 - i));
            sqliteBook.setSerialNumber("serial" + i).setAgeMinLimit(i).setAgeMaxLimit(i + 10).setGenre("genre").setPrequelId(i + 1);
            addBook(sqliteBook);
        }

        int typeTest = 20;
        for (int i = 1; i <= typeTest; i++) {
            SqliteTypeTest sqliteTypeTest = new SqliteTypeTest();
            sqliteTypeTest.setId(i);
            sqliteTypeTest.setString("I am number " + i);
            sqliteTypeTest.setStringEmpty("");
            sqliteTypeTest.setNumber(1);
            sqliteTypeTest.setFloatNumber(10);
            sqliteTypeTest.setBlob(sqliteTypeTest.getString().getBytes());
            sqliteTypeTest.setBlobEmpty(new byte[0]);
            addTypeTest(sqliteTypeTest);
        }
    }

    // Adding new contact
    public void addContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CONTACT_KEY_ID, contact.getId()); // Contact Name
        values.put(CONTACT_KEY_NAME, contact.getName()); // Contact Name
        values.put(CONTACT_KEY_ADDR, contact.getAddress()); // Contact Phone Number

        // Inserting Row
        db.insert(TABLE_CONTACTS, null, values);
        db.close(); // Closing database connection
    }

    // Adding new book
    public void addBook(SqliteBook sqliteBook) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(BOOK_KEY_ID, sqliteBook.getId()); // Book id
        values.put(BOOK_KEY_TITLE, sqliteBook.getTitle()); // Book Name
        values.put(BOOK_KEY_AUTHOR, sqliteBook.getAuthor()); // Author Name
        values.put(BOOK_KEY_SERIAL_NUMBER, sqliteBook.getSerialNumber()); // Serial Number
        values.put(BOOK_KEY_PAGES, sqliteBook.getPages()); // Pages
        values.put(BOOK_KEY_GENRE, sqliteBook.getGenre()); // Genre
        values.put(BOOK_KEY_AGE_MIN_LIMIT, sqliteBook.getAgeMinLimit()); // Age limit min
        values.put(BOOK_KEY_AGE_MAX_LIMIT, sqliteBook.getAgeMaxLimit()); // Age limit max
        values.put(BOOK_KEY_SEQUEL_ID, sqliteBook.getSequelId()); // Sequel Id
        values.put(BOOK_KEY_PREQUEL_ID, sqliteBook.getPrequelId()); // Prequel Id

        // Inserting Row
        db.insert(TABLE_BOOKS, null, values);
        db.close(); // Closing database connection
    }

    // Adding new book
    public void addTypeTest(SqliteTypeTest sqliteTypeTest) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TYPE_TEST_KEY_ID, sqliteTypeTest.getId());
        values.put(TYPE_TEST_KEY_NUMBER, sqliteTypeTest.getNumber());
//        values.put(TYPE_TEST_KEY_NUMBER_NON , sqliteTypeTest.getNumberNon());
        values.putNull(TYPE_TEST_KEY_NUMBER_NON);
        values.put(TYPE_TEST_KEY_FLOAT, sqliteTypeTest.getFloatNumber());
//        values.put(TYPE_TEST_KEY_FLOAT_NON , sqliteTypeTest.getFloatNumberNon());
        values.putNull(TYPE_TEST_KEY_FLOAT_NON);
        values.put(TYPE_TEST_KEY_STRING, sqliteTypeTest.getString());
//        values.put(TYPE_TEST_KEY_STRING_NULL , sqliteTypeTest.getStringNull());
        values.putNull(TYPE_TEST_KEY_STRING_NULL);
        values.put(TYPE_TEST_KEY_STRING_EMPTY, sqliteTypeTest.getStringEmpty());
        values.put(TYPE_TEST_KEY_BLOB, sqliteTypeTest.getBlob());
        values.put(TYPE_TEST_KEY_BLOB_EMPTY, sqliteTypeTest.getBlobEmpty());
//        values.put(TYPE_TEST_KEY_BLOB_NULL, sqliteTypeTest.getBlobNull());
        values.putNull(TYPE_TEST_KEY_BLOB_NULL);

        // Inserting Row
        db.insert(TABLE_TYPE_TEST, null, values);
        db.close(); // Closing database connection
    }

    // Getting one contact
    public Contact getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONTACTS, new String[]{CONTACT_KEY_ID,
                        CONTACT_KEY_NAME, CONTACT_KEY_ADDR}, CONTACT_KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Contact contact = new Contact(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        // return contact
        return contact;
    }

    // Getting All Contacts
    public List<Contact> getAllContacts() {
        List<Contact> contactList = new ArrayList<Contact>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setId(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setAddress(cursor.getString(2));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    // Updating a contact
    public int updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CONTACT_KEY_NAME, contact.getName());
        values.put(CONTACT_KEY_ADDR, contact.getAddress());

        // updating row
        return db.update(TABLE_CONTACTS, values, CONTACT_KEY_ID + " = ?",
                new String[]{String.valueOf(contact.getId())});
    }

    // Deleting a contact
    public void deleteContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, CONTACT_KEY_ID + " = ?",
                new String[]{String.valueOf(contact.getId())});
        db.close();
    }

    private static class PopulateDataAsync extends AsyncTask<Void, Void, Void> {

        private final SqliteHelper mDb;

        PopulateDataAsync(SqliteHelper db) {
            mDb = db;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            mDb.populateTestData();
            return null;
        }

    }

    private String getTheBigText() {
        return "The quick brown fox jumps over the lazy dog, The quick brown fox jumps over the lazy dog, The quick brown fox jumps over the lazy dog, The quick brown fox jumps over the lazy dog, The quick brown fox jumps over the lazy dog, The quick brown fox jumps over the lazy dog";
    }
}
