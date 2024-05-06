package com.hectoruiz.data.api.remote

import com.hectoruiz.data.api.remote.ApiClient.Companion.BASE_URL
import com.hectoruiz.data.api.remote.ApiClient.Companion.TIMEOUT
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit

class ApiClientTest {

    init {
        MockKAnnotations.init(this)
    }

    @MockK
    lateinit var interceptor: HttpLoggingInterceptor

    @MockK
    lateinit var okHttpClient: OkHttpClient

    @MockK
    lateinit var retrofit: Retrofit

    private lateinit var apiClient: ApiClient

    @Before
    fun setUp() {
        apiClient = ApiClient(okHttpClient)

        every { okHttpClient.connectTimeoutMillis } returns TIMEOUT.toInt()
        every { okHttpClient.readTimeoutMillis } returns TIMEOUT.toInt()
        every { okHttpClient.interceptors } returns listOf(interceptor)
        every { retrofit.baseUrl() } returns BASE_URL.toHttpUrlOrNull()!!
        every { retrofit.newBuilder().build() } returns retrofit
        every { retrofit.callFactory() } returns okHttpClient
    }

    @Test
    fun `check interceptors on okHttpClient`() {
        assertEquals(1, okHttpClient.interceptors.size)
    }

    @Test
    fun `check timeout configuration on okHttpClient`() {
        assertEquals(TIMEOUT, okHttpClient.connectTimeoutMillis.toLong())
        assertEquals(TIMEOUT, okHttpClient.readTimeoutMillis.toLong())
    }

    @Test
    fun `check okHttpClient configuration on retrofit`() {
        assertEquals(apiClient.retrofit.callFactory() as OkHttpClient, okHttpClient)
    }

    @Test
    fun `check baseUrl configuration on retrofit`() {
        assertEquals(BASE_URL, apiClient.retrofit.baseUrl().toString())
    }
}
