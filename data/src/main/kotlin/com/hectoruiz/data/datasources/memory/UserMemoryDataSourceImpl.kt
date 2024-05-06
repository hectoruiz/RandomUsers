package com.hectoruiz.data.datasources.memory

import com.hectoruiz.data.api.memory.UserDao
import com.hectoruiz.data.entities.UserEntity
import com.hectoruiz.data.repositories.UserMemoryDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserMemoryDataSourceImpl @Inject constructor(private val userDao: UserDao) :
    UserMemoryDataSource {

    override fun getUsers(): Flow<List<UserEntity>> = userDao.getAll()

    override suspend fun getNumUsers(): Int {
        return userDao.getNumUsers()
    }

    override suspend fun addUsers(users: List<UserEntity>) {
        userDao.insertAll(users)
    }

    override suspend fun deleteUser(user: UserEntity): Boolean {
        return userDao.update(user.copy(isActive = false)) == 1
    }

    override fun getUser(email: String): Flow<UserEntity?> {
        return userDao.getUser(email)
    }
}
