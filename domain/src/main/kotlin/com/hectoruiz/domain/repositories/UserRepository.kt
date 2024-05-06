package com.hectoruiz.domain.repositories

import com.hectoruiz.domain.models.UserModel
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun getUsers(): Flow<List<UserModel>>

    suspend fun getAmountUsers(): Int

    suspend fun getRemoteUsers(results: Int): Result<Unit>

    suspend fun deleteUser(userModel: UserModel): Boolean

    fun getUser(email: String): Flow<Result<UserModel>>
}
