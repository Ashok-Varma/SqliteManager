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
     */
    public String getDatabaseName();

    /**
     * Releases a reference to the database object, closing the object if the last database reference was released.
     */
    public void freeResources();
}
