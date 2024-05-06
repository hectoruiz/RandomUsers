package com.hectoruiz.data.api.remote

import com.hectoruiz.data.models.UsersApiModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("/?page=1&seed=abc&exc=dob,login,id,nat&noinfo")
    suspend fun getUsers(@Query(RESULTS) results: Int): Response<UsersApiModel>

    private companion object {
        const val RESULTS = "results"
    }
}
