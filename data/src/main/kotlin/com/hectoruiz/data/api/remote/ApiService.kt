package com.hectoruiz.data.api.remote

import com.hectoruiz.data.models.UsersApiModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("api/")
    suspend fun getUsers(
        @Query(PAGE) page: Int,
        @Query(RESULTS) numItems: Int,
        @Query(SEED) seed: String,
    ): Response<UsersApiModel>

    private companion object {
        const val PAGE = "page"
        const val RESULTS = "results"
        const val SEED = "seed"
    }
}


