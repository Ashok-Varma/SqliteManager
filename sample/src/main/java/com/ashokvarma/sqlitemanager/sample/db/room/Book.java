package com.ashokvarma.sqlitemanager.sample.db.room;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Book {
    public @PrimaryKey String id;
    public String title;
}
