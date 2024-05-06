package com.hectoruiz.randomusers.di

import com.hectoruiz.data.api.remote.ApiClient
import com.hectoruiz.data.api.remote.ApiClient.Companion.TIMEOUT
import com.hectoruiz.data.api.remote.ApiService
import com.hectoruiz.data.datasources.remote.UserRemoteDataSourceImpl
import com.hectoruiz.data.repositories.UserRemoteDataSource
import com.hectoruiz.randomusers.BuildConfig
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {

    companion object {

        @Provides
        fun providerLoggingInterceptor(): Interceptor {
            return HttpLoggingInterceptor().apply {
                setLevel(
                    if (BuildConfig.DEBUG) {
                        HttpLoggingInterceptor.Level.BODY
                    } else {
                        HttpLoggingInterceptor.Level.NONE
                    }
                )
            }
        }

        @Provides
        fun providerOkHttpClient(interceptor: Interceptor): OkHttpClient {
            return OkHttpClient.Builder().connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .readTimeout(TIMEOUT, TimeUnit.SECONDS).build()

        }

        @Provides
        fun providerApiClient(okHttpClient: OkHttpClient): ApiClient {
            return ApiClient(okHttpClient)
        }

        @Provides
        fun providerApiService(apiClient: ApiClient): ApiService {
            return apiClient.retrofit.create()
        }
    }

    @Singleton
    @Binds
    abstract fun bindUserRemoteDataSource(userRemoteDataSourceImpl: UserRemoteDataSourceImpl): UserRemoteDataSource
}
