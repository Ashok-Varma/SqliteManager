package com.ashokvarma.sqlitemanager.sample.db.room.utils;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.ashokvarma.sqlitemanager.sample.db.room.AppDatabase;
import com.ashokvarma.sqlitemanager.sample.db.room.Book;
import com.ashokvarma.sqlitemanager.sample.db.room.Loan;
import com.ashokvarma.sqlitemanager.sample.db.room.User;

import java.util.Calendar;
import java.util.Date;

public class DatabaseInitializer {

    // Simulate a blocking operation delaying each Loan insertion with a delay:
    private static final int DELAY_MILLIS = 500;

    public static void populateAsync(final AppDatabase db) {

        PopulateDbAsync task = new PopulateDbAsync(db);
        task.execute();
    }

    public static void populateSync(@NonNull final AppDatabase db) {
        populateWithTestData(db);
    }

    public static void clearAsync(final AppDatabase db) {

        ClearDbAsync task = new ClearDbAsync(db);
        task.execute();
    }

    public static void clearSync(@NonNull final AppDatabase db) {
        clearAll(db);
    }

    private static void addLoan(final AppDatabase db, final String id,
                                final User user, final Book book, Date from, Date to) {
        Loan loan = new Loan();
        loan.id = id;
        loan.bookId = book.id;
        loan.userId = user.id;
        loan.startTime = from;
        loan.endTime = to;
        db.loanModel().insertLoan(loan);
    }

    private static Book addBook(final AppDatabase db, final String id, final String title) {
        Book book = new Book();
        book.id = id;
        book.title = title;
        db.bookModel().insertBook(book);
        return book;
    }

    private static User addUser(final AppDatabase db, final String id, final String name,
                                final String lastName, final int age) {
        User user = new User();
        user.id = id;
        user.age = age;
        user.name = name;
        user.lastName = lastName;
        db.userModel().insertUser(user);
        return user;
    }

    private static void clearAll(AppDatabase db) {
        db.loanModel().deleteAll();
        db.userModel().deleteAll();
        db.bookModel().deleteAll();
    }

    private static void populateWithTestData(AppDatabase db) {
        clearAll(db);

        User user1 = addUser(db, "1", "Jason", "Seaver", 40);
        User user2 = addUser(db, "2", "Mike", "Seaver", 12);
        addUser(db, "3", "Carol", "Seaver", 15);

        Book book1 = addBook(db, "1", "Dune");
        Book book2 = addBook(db, "2", "1984");
        Book book3 = addBook(db, "3", "The War of the Worlds");
        Book book4 = addBook(db, "4", "Brave New World");
        addBook(db, "5", "Foundation");
        try {
            // Loans are added with a delay, to have time for the UI to react to changes.

            Date today = getTodayPlusDays(0);
            Date yesterday = getTodayPlusDays(-1);
            Date twoDaysAgo = getTodayPlusDays(-2);
            Date lastWeek = getTodayPlusDays(-7);
            Date twoWeeksAgo = getTodayPlusDays(-14);

            addLoan(db, "1", user1, book1, twoWeeksAgo, lastWeek);
            Thread.sleep(DELAY_MILLIS);
            addLoan(db, "2", user2, book1, lastWeek, yesterday);
            Thread.sleep(DELAY_MILLIS);
            addLoan(db, "3", user2, book2, lastWeek, today);
            Thread.sleep(DELAY_MILLIS);
            addLoan(db, "4", user2, book3, lastWeek, twoDaysAgo);
            Thread.sleep(DELAY_MILLIS);
            addLoan(db, "5", user2, book4, lastWeek, today);
            Log.d("DB", "Added loans");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static Date getTodayPlusDays(int daysAgo) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, daysAgo);
        return calendar.getTime();
    }

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final AppDatabase mDb;

        PopulateDbAsync(AppDatabase db) {
            mDb = db;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            populateWithTestData(mDb);
            return null;
        }

    }

    private static class ClearDbAsync extends AsyncTask<Void, Void, Void> {

        private final AppDatabase mDb;

        ClearDbAsync(AppDatabase db) {
            mDb = db;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            clearAll(mDb);
            return null;
        }

    }
}
