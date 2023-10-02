package com.hectoruiz.data.repositories

import com.hectoruiz.data.entities.UserEntity
import kotlinx.coroutines.flow.Flow

interface UserMemoryDataSource {

    fun getUsers(): Flow<List<UserEntity>>

    suspend fun addUsers(users: List<UserEntity>)

    suspend fun deleteUser(user: UserEntity): Boolean

    fun getUser(userId: String): Flow<UserEntity?>
}