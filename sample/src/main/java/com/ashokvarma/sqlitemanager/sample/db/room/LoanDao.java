package com.ashokvarma.sqlitemanager.sample.db.room;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;

import java.util.Date;
import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.ABORT;

@Dao
@TypeConverters(DateConverter.class)
public interface LoanDao {

    @Query("SELECT * From Loan")
    List<Loan> findAll();

    @Query("SELECT Loan.id, Book.title, User.name, Loan.startTime, Loan.endTime From Loan " +
        "INNER JOIN Book ON Loan.book_id = Book.id " +
        "INNER JOIN User ON Loan.user_id = User.id ")
    List<LoanWithUserAndBook> findAllWithUserAndBook();

    @Query("SELECT Loan.id, Book.title as title, User.name as name, Loan.startTime, Loan.endTime " +
            "FROM Book " +
            "INNER JOIN Loan ON Loan.book_id = Book.id " +
            "INNER JOIN User on User.id = Loan.user_id " +
            "WHERE User.name LIKE :userName " +
            "AND Loan.endTime > :after "
    )
    public List<LoanWithUserAndBook> findLoansByNameAfter(String userName, Date after);

    @Insert(onConflict = ABORT)
    void insertLoan(Loan loan);

    @Query("DELETE FROM Loan")
    void deleteAll();
}
