package com.jaykallen.stocks.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.jaykallen.stocks.models.Portfolio

@Database(entities = arrayOf(Portfolio::class), version = 1)
abstract class RoomInstance: RoomDatabase() {

    abstract fun daoAccess(): RoomDao

    companion object {
        var instance: RoomInstance? = null

        fun getInstance(context: Context): RoomInstance? {
            if (instance == null) {
                synchronized(RoomInstance::class) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            RoomInstance::class.java, "stocks.db").build()
                }
            }
            return instance
        }

        fun destroyInstance() {
            instance = null
        }
    }
}