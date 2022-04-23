package com.example.budgetmanager_kotlin

import androidx.room.*

@Dao
interface TransactionDao {
    @Query("SELECT * from transactions")
    fun getAll(): List<Transaction>

    @Insert
    fun insertAll(vararg transaction: Transaction)

    @Delete
    fun delete(transaction: Transaction)

    @Update
    fun update(vararg transaction: Transaction)

    @Query("SELECT * FROM transactions WHERE date LIKE :current_date")
    fun getWithDate(current_date: String): List<Transaction>
}