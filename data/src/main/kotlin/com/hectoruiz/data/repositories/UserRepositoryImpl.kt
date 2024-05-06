package com.hectoruiz.data.repositories

import com.hectoruiz.data.entities.toEntity
import com.hectoruiz.data.entities.toModel
import com.hectoruiz.data.models.toModel
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

    override suspend fun getAmountUsers() = userMemoryDataSource.getNumUsers()

    override suspend fun getRemoteUsers(results: Int): Result<Unit> {
        return userRemoteDataSource.getUsers(results)
            .fold(
                onSuccess = { usersApiModel ->
                    storeUsers(usersApiModel.toModel())
                    Result.success(Unit)
                },
                onFailure = { throwable ->
                    Result.failure(throwable)
                })
    }

    private suspend fun storeUsers(users: List<UserModel>) {
        userMemoryDataSource.addUsers(users.toEntity())
    }

    override suspend fun deleteUser(userModel: UserModel): Boolean {
        return userMemoryDataSource.deleteUser(userModel.toEntity())
    }

    override fun getUser(email: String): Flow<Result<UserModel>> {
        val user = userMemoryDataSource.getUser(email)
        return user.map {
            if (it != null) Result.success(it.toModel())
            else Result.failure(Throwable(USER_NOT_FOUND))
        }
    }

    companion object {
        const val USER_NOT_FOUND = "User not found"
    }
}
