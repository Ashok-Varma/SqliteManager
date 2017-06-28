# SqliteManager

**get sample apk from [Google Play Store][googlePlayStoreLink]**

## What is this library about?

Sqlite Manager helps to manage your android Sqlite Database very effectively with ease

**(currently under active development, expect to see new releases almost daily)**

## Features

* Check all the table entries in your database with preferred sort order
* Do custom queries on your database and results are displayed in UI

## Download

Based on your IDE you can import library in one of the following ways

Gradle:
```groovy
debugCompile 'com.ashokvarma.android:sqlite-manager:1.0.0RC1'
releaseCompile 'com.ashokvarma.android:sqlite-manager-no-op:1.0.0RC1'
```
If you want this in library in production also then try this : 
```groovy
compile 'com.ashokvarma.android:sqlite-manager:1.0.0RC1'
```


or grab via Maven:
```xml
<dependency>
  <groupId>com.ashokvarma.android</groupId>
  <artifactId>sqlite-manager</artifactId>
  <version>1.0.0RC1</version>
  <type>pom</type>
</dependency>
```

or Ivy:
```xml
<dependency org='com.ashokvarma.android' name='sqlite-manager' rev='1.0.0RC1'>
  <artifact name='$AID' ext='pom'></artifact>
</dependency>
```

or Download [the latest JAR][mavenAarDownload]


## Usage

Implement SqliteDataRetriever interface either directly in SqliteOpenHelper (OR) if you are using thrid party ORM's ..etc just provide the inteface to library

Here is an example interface if SqliteOpenHelper is used (for other library implementations - check sample project)
```java
    public class HelperSqliteDataRetriever implements SqliteDataRetriever {
        SqliteHelper mSqliteHelper;
        SQLiteDatabase mSQLiteDatabase;

        HelperSqliteDataRetriever(SqliteHelper sqliteHelper) {
            mSqliteHelper = sqliteHelper;
            mSQLiteDatabase = mSqliteHelper.getWritableDatabase();
        }

        @Override
        public Cursor rawQuery(@NonNull String query, String[] selectionArgs) {
            if (mSQLiteDatabase == null || !mSQLiteDatabase.isOpen()) {
                mSQLiteDatabase = mSqliteHelper.getWritableDatabase();
            }
            return mSQLiteDatabase.rawQuery(query, selectionArgs);
        }

        @Override
        public String getDatabaseName() {
            return mSqliteHelper.getDatabaseName();
        }

        @Override
        public void freeResources() {
            // not good practice to open multiple database connections and close every time
        }
    }
```

Then just pass the interface instance with the context to launchSqliteManager method

```java
    SqliteManager.launchSqliteManager(this, new HelperSqliteDataRetriever(sqliteHelper));
```

## License

```
Sqlite Manager library for Android
Copyright (c) 2016 Ashok Varma (http://ashokvarma.me/).

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
[mavenLatestJarDownload]: https://search.maven.org/remote_content?g=com.ashokvarma.android&a=sqlite-manager&v=LATEST
[googlePlayStoreLink]: https://play.google.com/store/apps/details?id=com.ashokvarma.sqlitemanager.sample
