package com.ashokvarma.sqlitemanager;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Class description
 *
 * @author ashok
 * @version 1.0
 * @since 27/11/17
 */
class SqliteManagerUtils {

    static void shareSqliteResponseDataAsJson(AppCompatActivity context, SqliteResponseData sqliteResponseData) {
        Intent shareIntent =
                ShareCompat.IntentBuilder
                        .from(context)
                        .setType("text/*")
                        .getIntent();

        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, getSqliteResponseDataJsonString(sqliteResponseData));
        // validate that the device can open your File!
        PackageManager pm = context.getPackageManager();
        if (shareIntent.resolveActivity(pm) != null) {
            context.startActivity(shareIntent);
        }
    }

    private static String getSqliteResponseDataJsonString(SqliteResponseData sqliteResponseData) {
        try {
            return getSqliteResponseDataJson(sqliteResponseData).toString(4);
        } catch (JSONException e) {
            return "";
        }
    }

    private static JSONArray getSqliteResponseDataJson(SqliteResponseData sqliteResponseData) {
        JSONArray resultSet = new JSONArray();

        if (!sqliteResponseData.isQuerySuccess()) {
            return resultSet;
        }

        for (SparseArray<String> columnIndexToValues : sqliteResponseData.getColumnIndexToValuesArray()) {
            JSONObject rowObject = new JSONObject();
            for (int i = 0; i < columnIndexToValues.size(); i++) {
                String columnEntry = columnIndexToValues.get(i);
                try {
                    if (columnEntry == null) {
                        rowObject.put(sqliteResponseData.getTableColumnNames()[i], "");
                    } else {
                        rowObject.put(sqliteResponseData.getTableColumnNames()[i], columnEntry);
                    }
                } catch (JSONException e) {
//                    e.printStackTrace();
                }
            }
            resultSet.put(rowObject);
        }

        return resultSet;
    }

    static void shareSqliteResponseDataAsCsvFile(AppCompatActivity context, SqliteResponseData sqliteResponseData, String selectedTableName, String CSVFileShareAuthority) {
        if (!sqliteResponseData.isQuerySuccess() || CSVFileShareAuthority == null || CSVFileShareAuthority.trim().length() == 0) {
            return;
        }

        StringBuilder csvString = new StringBuilder();
        boolean firstElement = true;
        for (String columnName : sqliteResponseData.getTableColumnNames()) {
            if (!firstElement) {
                csvString.append(",");
            }
            firstElement = false;
            csvString.append("\"").append(columnName).append("\"");
        }
        csvString.append("\n");


        for (SparseArray<String> columnIndexToValues : sqliteResponseData.getColumnIndexToValuesArray()) {
            firstElement = true;
            for (int i = 0; i < columnIndexToValues.size(); i++) {
                if (!firstElement) {
                    csvString.append(",");
                }
                firstElement = false;
                String columnEntry = columnIndexToValues.get(i);
                if (columnEntry == null) {
                    csvString.append("\"").append("\"");
                } else {
                    csvString.append("\"").append(columnEntry).append("\"");
                }
            }
            csvString.append("\n");
        }

        String csvFileName = "sqlite_export" + (selectedTableName == null ? "" : "_" + selectedTableName) + ".csv";
        try {
            File CSVFileDir = new File(context.getFilesDir(), "sqliteManager");
            CSVFileDir.mkdir();
            File CSVFile = new File(CSVFileDir, csvFileName);
            CSVFile.createNewFile();

            FileOutputStream fileOutputStream = new FileOutputStream(CSVFile);
            fileOutputStream.write(csvString.toString().getBytes());
            fileOutputStream.close();

            // generate URI, defined authority as the application ID in the Manifest
            Uri uriToCSVFIle = FileProvider.getUriForFile(context, CSVFileShareAuthority, CSVFile);

            Intent shareIntent =
                    ShareCompat.IntentBuilder
                            .from(context)
                            .setStream(uriToCSVFIle)
                            .setType("text/csv")
                            .getIntent();

            // Provide read access
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            // validate that the device can open your File!
            PackageManager pm = context.getPackageManager();
            if (shareIntent.resolveActivity(pm) != null) {
                context.startActivity(shareIntent);
            }
        } catch (Exception e) {
//                    e.printStackTrace();
        }
    }

}
