package com.hectoruiz.domain.repositories

import com.hectoruiz.domain.models.UserModel
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun getUsers(): Flow<List<UserModel>>

    suspend fun getRemoteUsers(page: Int): Result<Unit>

    suspend fun deleteUser(userModel: UserModel): Boolean

    fun getUser(userId: String): Flow<Result<UserModel>>
}