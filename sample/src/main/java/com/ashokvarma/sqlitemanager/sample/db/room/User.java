package com.ashokvarma.sqlitemanager.sample.db.room;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;


@Entity
public class User {
    public @PrimaryKey String id;
    public String name;
    public String lastName;
    public int age;
}
