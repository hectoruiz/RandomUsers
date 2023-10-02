package com.hectoruiz.data.datasources.memory

import com.hectoruiz.data.api.memory.UserDao
import com.hectoruiz.data.entities.UserEntity
import com.hectoruiz.data.entities.toModel
import com.hectoruiz.data.models.UsersApiModel
import com.hectoruiz.data.repositories.UserMemoryDataSource
import com.hectoruiz.domain.models.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserMemoryDataSourceImpl @Inject constructor(private val userDao: UserDao) :
    UserMemoryDataSource {

    override fun getUsers(): Flow<List<UserEntity>> = userDao.getAll()

    override suspend fun addUsers(users: List<UserEntity>) {
        userDao.insertAll(users)
    }

    override suspend fun deleteUser(user: UserEntity): Boolean {
        return userDao.update(user.copy(isActive = false)) == 1
    }

    override fun getUser(userId: String): Flow<UserEntity?> {
        return userDao.getUser(userId)
    }
}