package com.hectoruiz.data.api.memory

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hectoruiz.data.entities.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM users")
    fun getAll(): Flow<List<UserEntity>>

    @Query("SELECT COUNT(email) FROM users")
    suspend fun getNumUsers(): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(users: List<UserEntity>)

    @Update
    suspend fun update(user: UserEntity): Int

    @Query("SELECT * FROM users WHERE email = :email")
    fun getUser(email: String): Flow<UserEntity?>
}
