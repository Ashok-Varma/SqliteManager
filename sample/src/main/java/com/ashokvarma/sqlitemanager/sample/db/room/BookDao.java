package com.ashokvarma.sqlitemanager.sample.db.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.Update;

import java.util.Date;
import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;
import static android.arch.persistence.room.OnConflictStrategy.REPLACE;


@Dao
@TypeConverters(DateConverter.class)
public interface BookDao {

    @Query("select * from Book where id = :id")
    Book loadUserById(int id);

    @Query("SELECT * FROM Book " +
            "INNER JOIN Loan ON Loan.book_id = Book.id " +
            "INNER JOIN User on User.id = Loan.user_id " +
            "WHERE User.name LIKE :userName"
    )
    public List<Book> findBooksBorrowedByName(String userName);

    @Query("SELECT * FROM Book " +
            "INNER JOIN Loan ON Loan.book_id = Book.id " +
            "INNER JOIN User on User.id = Loan.user_id " +
            "WHERE User.name LIKE :userName " +
            "AND Loan.endTime > :after "
    )
    public List<Book> findBooksBorrowedByNameAfter(String userName, Date after);

    @Query("SELECT * FROM Book " +
            "INNER JOIN Loan ON Loan.book_id = Book.id " +
            "INNER JOIN User on User.id = Loan.user_id " +
            "WHERE User.name LIKE :userName"
    )
    public List<Book> findBooksBorrowedByNameSync(String userName);

    @Query("SELECT * FROM Book " +
            "INNER JOIN Loan ON Loan.book_id LIKE Book.id " +
            "WHERE Loan.user_id LIKE :userId "
    )
    public List<Book> findBooksBorrowedByUser(String userId);

    @Query("SELECT * FROM Book " +
            "INNER JOIN Loan ON Loan.book_id LIKE Book.id " +
            "WHERE Loan.user_id LIKE :userId " +
            "AND Loan.endTime > :after "
    )
    public List<Book> findBooksBorrowedByUserAfter(String userId, Date after);

    @Query("SELECT * FROM Book " +
            "INNER JOIN Loan ON Loan.book_id LIKE Book.id " +
            "WHERE Loan.user_id LIKE :userId "
    )
    public List<Book> findBooksBorrowedByUserSync(String userId);

    @Query("SELECT * FROM Book")
    public List<Book> findAllBooks();


    @Query("SELECT * FROM Book")
    public List<Book> findAllBooksSync();

    @Insert(onConflict = IGNORE)
    void insertBook(Book book);

    @Update(onConflict = REPLACE)
    void updateBook(Book book);

    @Query("DELETE FROM Book")
    void deleteAll();
}
