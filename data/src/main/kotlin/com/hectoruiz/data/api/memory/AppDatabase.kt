package com.hectoruiz.data.api.memory

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hectoruiz.data.entities.UserEntity

@Database(entities = [UserEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
}