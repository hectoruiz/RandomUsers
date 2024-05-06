package com.hectoruiz.randomusers.di

import android.content.Context
import androidx.room.Room
import com.hectoruiz.data.api.memory.AppDatabase
import com.hectoruiz.data.api.memory.UserDao
import com.hectoruiz.data.datasources.memory.UserMemoryDataSourceImpl
import com.hectoruiz.data.repositories.UserMemoryDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MemoryModule {

    companion object {

        @Provides
        fun provideAppDatabase(@ApplicationContext applicationContext: Context): AppDatabase {
            return Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, DATABASE_NAME
            ).fallbackToDestructiveMigration().build()
        }

        @Provides
        fun provideUserDao(appDatabase: AppDatabase): UserDao {
            return appDatabase.userDao()
        }

        private const val DATABASE_NAME = "database-random-user"
    }

    @Singleton
    @Binds
    abstract fun bindUserMemoryDataSource(userMemoryDataSourceImpl: UserMemoryDataSourceImpl): UserMemoryDataSource
}
