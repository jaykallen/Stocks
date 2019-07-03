package com.jaykallen.stocks.room

import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import com.jaykallen.stocks.models.Portfolio

@Dao
interface RoomDao {

    @Query ("Select * from Portfolio")
    fun getAll(): List<Portfolio>

    @Query ("Select * from Portfolio where id=:id")
    fun getById(id: String): Portfolio

    @Insert (onConflict = REPLACE)
    fun create (portfolio: Portfolio)

    @Update
    fun update (portfolio: Portfolio)

    @Delete
    fun delete(portfolio: Portfolio)

}