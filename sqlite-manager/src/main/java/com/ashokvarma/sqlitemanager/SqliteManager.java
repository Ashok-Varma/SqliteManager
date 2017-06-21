package com.ashokvarma.sqlitemanager;

import android.content.Context;
import android.content.Intent;

/**
 * Class description
 *
 * @author ashokvarma
 * @version 1.0
 * @since 17 Jun 2017
 */
public class SqliteManager {

    static SqliteDataRetriever mSqliteDataRetriever;

    public static void launchSqliteManager(Context context, SqliteDataRetriever sqliteDataRetriever) {
        mSqliteDataRetriever = sqliteDataRetriever;
        context.startActivity(new Intent(context, SqliteManagerActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    static void clearReferences() {
        mSqliteDataRetriever = null;
    }


}
