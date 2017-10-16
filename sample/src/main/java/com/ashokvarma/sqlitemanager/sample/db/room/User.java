package com.ashokvarma.sqlitemanager.sample.db.room;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;


@Entity
public class User {
    public @PrimaryKey @NonNull String id;
    public String name;
    public String lastName;
    public int age;
}
