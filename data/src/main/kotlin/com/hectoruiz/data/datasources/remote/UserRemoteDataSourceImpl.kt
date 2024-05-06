package com.hectoruiz.data.datasources.remote

import com.hectoruiz.data.api.remote.ApiService
import com.hectoruiz.data.models.UsersApiModel
import com.hectoruiz.data.repositories.UserRemoteDataSource
import javax.inject.Inject

class UserRemoteDataSourceImpl @Inject constructor(private val apiService: ApiService) :
    UserRemoteDataSource {

    override suspend fun getUsers(results: Int): Result<UsersApiModel> {
        val response = apiService.getUsers(results)
        return if (response.isSuccessful) {
            val body = response.body()
            if (body != null) Result.success(body)
            else Result.failure(Throwable())
        } else {
            Result.failure(Throwable(response.errorBody().toString()))
        }
    }
}
