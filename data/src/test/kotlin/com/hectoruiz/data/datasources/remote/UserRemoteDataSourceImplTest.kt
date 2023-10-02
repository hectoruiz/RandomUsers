package com.hectoruiz.data.datasources.remote

import com.hectoruiz.data.api.remote.ApiClient
import com.hectoruiz.data.api.remote.ApiService
import com.hectoruiz.data.models.UsersApiModel
import com.hectoruiz.data.repositories.UserRemoteDataSource
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.create

class UserRemoteDataSourceImplTest {

    init {
        MockKAnnotations.init(this)
    }

    @MockK
    private lateinit var apiClient: ApiClient

    @MockK
    private lateinit var retrofit: Retrofit

    @MockK
    private lateinit var apiService: ApiService

    private val userRemoteDataSource: UserRemoteDataSource = UserRemoteDataSourceImpl(apiService)

    @Before
    fun setUp() {
        every { apiClient.retrofit } returns retrofit
        every { retrofit.create<ApiService>() } returns apiService
    }

    @Test
    fun `error retrieving users from remote data source`() {
        val errorCode = 400
        val errorMessage = "BadRequest"
        val errorResponse = Response.error<UsersApiModel>(errorCode, errorMessage.toResponseBody())
        coEvery { apiService.getUsers(any(), any(), any()) } returns errorResponse

        val result = runBlocking {
            userRemoteDataSource.getUsers(1)
        }
        assertTrue(result.isFailure)
        result.onFailure {
            assertEquals(errorResponse.errorBody().toString(), it.message)
        }
    }

    @Test
    fun `success retrieving users from remote data source`() {
        val userApiModel = mockk<UsersApiModel>()
        val successResponse = Response.success(userApiModel)
        coEvery { apiService.getUsers(any(), any(), any()) } returns successResponse

        val result = runBlocking { userRemoteDataSource.getUsers(1) }

        assertTrue(result.isSuccess)
        result.onSuccess {
            assertEquals(userApiModel, it)
        }
    }
}