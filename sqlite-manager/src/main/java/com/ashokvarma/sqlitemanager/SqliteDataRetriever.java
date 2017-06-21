package com.ashokvarma.sqlitemanager;

import android.database.Cursor;
import android.support.annotation.NonNull;

/**
 * Class description
 *
 * @author ashokvarma
 * @version 1.0
 * @see android.database.sqlite.SQLiteDatabase
 * @since 17 Jun 2017
 */
public interface SqliteDataRetriever {
    /**
     * Runs the provided SQL and returns a {@link Cursor} over the result set.
     *
     * Called Multiple Times
     *
     * @param query         the SQL query. The SQL string must not be ; terminated
     * @param selectionArgs You may include ?s in where clause in the query,
     *                      which will be replaced by the values from selectionArgs. The
     *                      values will be bound as Strings.
     * @return A {@link Cursor} object, which is positioned before the first entry. Note that
     * {@link Cursor}s are not synchronized, see the documentation for more details.
     */
    public Cursor rawQuery(@NonNull String query, String[] selectionArgs);


    /**
     * Return the name of the SQLite database being opened, as given to the constructor.
     *
     * Called once per launch
     */
    public String getDatabaseName();


    /**
     * This method is used to close the database (or) release any resources after every query.
     * (called after #rawQuery returned cursor is processed)
     *
     * It's <b>not a good practice</> to get new instance of writable database on every query and close it after the query
     * But if followed some pattern which don't allow to keep many open connections. This can be used to close the connection and free the resources
     *
     * Called Multiple Times
     *
     * So annotated as deprecated (Don't use it until unless forced to)
     */
    @Deprecated
    public void freeResources();
}
