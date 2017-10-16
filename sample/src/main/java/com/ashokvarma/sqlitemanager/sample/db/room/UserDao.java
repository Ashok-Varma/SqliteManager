package com.ashokvarma.sqlitemanager.sample.db.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;

@Dao
public interface UserDao {
    @Query("select * from user")
    List<User> loadAllUsers();

    @Query("select * from user where id = :id")
    User loadUserById(int id);

    @Query("select * from user where name = :firstName and lastName = :lastName")
    List<User> findByNameAndLastName(String firstName, String lastName);

    @Insert(onConflict = IGNORE)
    void insertUser(User user);

    @Delete
    void deleteUser(User user);

    @Query("delete from user where name like :badName OR lastName like :badName")
    int deleteUsersByName(String badName);

    @Insert(onConflict = IGNORE)
    void insertOrReplaceUsers(User... users);

    @Delete
    void deleteUsers(User user1, User user2);

    @Query("SELECT * FROM User WHERE age == :age")
    List<User> findYoungerThan(int age);

    @Query("SELECT * FROM User WHERE age < :age")
    List<User> findYoungerThanSolution(int age);

    @Query("DELETE FROM User")
    void deleteAll();
}