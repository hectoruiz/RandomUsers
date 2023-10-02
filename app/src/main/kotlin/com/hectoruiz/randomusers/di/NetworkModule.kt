package com.hectoruiz.randomusers.di

import com.hectoruiz.data.api.remote.ApiClient
import com.hectoruiz.data.api.remote.ApiService
import com.hectoruiz.data.datasources.remote.UserRemoteDataSourceImpl
import com.hectoruiz.data.repositories.UserRemoteDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {

    companion object {

        @Provides
        fun providerApiClient(): ApiClient {
            return ApiClient()
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