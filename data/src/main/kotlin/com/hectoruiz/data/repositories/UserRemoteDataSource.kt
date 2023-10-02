package com.hectoruiz.data.repositories

import com.hectoruiz.data.models.UsersApiModel

interface UserRemoteDataSource {

    suspend fun getUsers(page: Int): Result<UsersApiModel>
}