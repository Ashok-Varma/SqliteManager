package com.ashokvarma.sqlitemanager;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;

import com.ashokvarma.sqlitemanager.csv.CsvEscaper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;

/**
 * Class description
 *
 * @author ashok
 * @version 1.0
 * @since 27/11/17
 */
class SqliteManagerUtils {

    static void shareSqliteResponseDataAsJsonFile(AppCompatActivity context, SqliteResponseData sqliteResponseData, String selectedTableName, String fileShareAuthority) {
        if (fileShareAuthority == null || fileShareAuthority.trim().length() == 0) {
            return;
        }

        String toBeWrittenString = getSqliteResponseDataJsonString(sqliteResponseData);
        String fileName = "json_export" + (selectedTableName == null ? "" : "_" + selectedTableName) + ".txt";
        createTempFileAndShare(context, fileShareAuthority, toBeWrittenString, fileName, "text/*");
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

        for (SparseArray<Object> columnIndexToValues : sqliteResponseData.getColumnIndexToValuesArray()) {
            JSONObject rowObject = new JSONObject();
            for (int i = 0; i < columnIndexToValues.size(); i++) {
                Object columnEntry = columnIndexToValues.get(i);
                try {
                    if (columnEntry == null) {
                        rowObject.put(sqliteResponseData.getTableColumnNames()[i], "");
                    } else if (columnEntry instanceof byte[]) {
                        rowObject.put(sqliteResponseData.getTableColumnNames()[i], Arrays.toString((byte[]) columnEntry));
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

    static void shareSqliteResponseDataAsCsvFile(AppCompatActivity context, SqliteResponseData sqliteResponseData, String selectedTableName, String FileShareAuthority) {
        if (!sqliteResponseData.isQuerySuccess() || FileShareAuthority == null || FileShareAuthority.trim().length() == 0) {
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


        for (SparseArray<Object> columnIndexToValues : sqliteResponseData.getColumnIndexToValuesArray()) {
            firstElement = true;
            for (int i = 0; i < columnIndexToValues.size(); i++) {
                if (!firstElement) {
                    csvString.append(",");
                }
                firstElement = false;
                Object columnEntry = columnIndexToValues.get(i);
                if (columnEntry != null) {
                    if (columnEntry instanceof String) {
                        // if string
                        csvString.append(escapeCsv((String) columnEntry));
                    } else if (columnEntry instanceof byte[]) {
                        // if blob/byte[]
                        csvString.append(escapeCsv(Arrays.toString((byte[]) columnEntry)));
                    } else {
                        // if int, float and null
                        csvString.append(columnEntry);
                    }
                }
            }
            csvString.append("\n");
        }

        String fileName = "sqlite_export" + (selectedTableName == null ? "" : "_" + selectedTableName) + ".csv";
        createTempFileAndShare(context, FileShareAuthority, csvString.toString(), fileName, "text/csv");
    }

    private static void createTempFileAndShare(AppCompatActivity context, String fileShareAuthority, String csvString, String csvFileName, String type) {
        try {
            File fileDir = new File(context.getFilesDir(), "sqliteManager");
            fileDir.mkdir();
            File file = new File(fileDir, csvFileName);
            file.createNewFile();

            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(csvString.getBytes());
            fileOutputStream.close();

            // generate URI, defined authority as the application ID in the Manifest
            Uri uriToCSVFIle = FileProvider.getUriForFile(context, fileShareAuthority, file);

            Intent shareIntent =
                    ShareCompat.IntentBuilder
                            .from(context)
                            .setStream(uriToCSVFIle)
                            .setType(type)
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

    private static String escapeCsv(final String input) {
        return CsvEscaper.getInstance().translate(input);
    }

//    private final static char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
//    static String bytesToHex(byte[] bytes) {
//        char[] hexChars = new char[bytes.length * 2];
//        for ( int j = 0; j < bytes.length; j++ ) {
//            int v = bytes[j] & 0xFF;
//            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
//            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
//        }
//        return new String(hexChars);
//    }

}
