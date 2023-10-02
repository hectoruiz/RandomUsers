package com.hectoruiz.data.repositories

import com.hectoruiz.data.entities.toEntity
import com.hectoruiz.data.entities.toModel
import com.hectoruiz.data.models.toModel
import com.hectoruiz.domain.Constants.USER_NOT_FOUND
import com.hectoruiz.domain.models.UserModel
import com.hectoruiz.domain.repositories.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val userMemoryDataSource: UserMemoryDataSource,
) : UserRepository {

    override fun getUsers() = userMemoryDataSource.getUsers().map { it.toModel() }

    override suspend fun getRemoteUsers(page: Int): Result<Unit> {
        return userRemoteDataSource.getUsers(page).fold(
            onSuccess = {
                storeUsers(it.toModel())
                Result.success(Unit)
            },
            onFailure = {
                Result.failure(it)
            })
    }

    private suspend fun storeUsers(users: List<UserModel>) {
        userMemoryDataSource.addUsers(users.toEntity())
    }

    override suspend fun deleteUser(userModel: UserModel): Boolean {
        return userMemoryDataSource.deleteUser(userModel.toEntity())
    }

    override fun getUser(userId: String): Flow<Result<UserModel>> {
        val user = userMemoryDataSource.getUser(userId)
        return user.map {
            if (it != null) Result.success(it.toModel())
            else Result.failure(Throwable(USER_NOT_FOUND))
        }
    }
}
