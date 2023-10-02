package com.hectoruiz.data.datasources.remote

import com.hectoruiz.data.api.remote.ApiService
import com.hectoruiz.data.models.UsersApiModel
import com.hectoruiz.data.repositories.UserRemoteDataSource
import javax.inject.Inject

class UserRemoteDataSourceImpl @Inject constructor(private val apiService: ApiService) :
    UserRemoteDataSource {

    override suspend fun getUsers(page: Int): Result<UsersApiModel> {
        val response = apiService.getUsers(page, NUM_ITEMS, SEED)
        return if (response.isSuccessful) {
            val body = response.body()
            if (body != null) Result.success(body)
            else Result.failure(Throwable())
        } else {
            Result.failure(Throwable(response.errorBody().toString()))
        }
    }

    private companion object {
        const val NUM_ITEMS = 5
        const val SEED = "abc"
    }
}