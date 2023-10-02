package com.hectoruiz.randomusers.di

import com.hectoruiz.data.repositories.UserRepositoryImpl
import com.hectoruiz.domain.repositories.UserRepository
import com.hectoruiz.domain.usecases.DeleteUserUseCase
import com.hectoruiz.domain.usecases.FetchUsersUseCase
import com.hectoruiz.domain.usecases.GetUserUseCase
import com.hectoruiz.domain.usecases.GetUsersUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UserModule {

    @Singleton
    @Binds
    abstract fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository

    companion object {

        @Provides
        fun provideFetchUsersUseCase(userRepository: UserRepository): FetchUsersUseCase {
            return FetchUsersUseCase(userRepository)
        }

        @Provides
        fun provideGetUsersUseCase(userRepository: UserRepository): GetUsersUseCase {
            return GetUsersUseCase(userRepository)
        }

        @Provides
        fun provideDeleteUserUseCase(userRepository: UserRepository): DeleteUserUseCase {
            return DeleteUserUseCase(userRepository)
        }

        @Provides
        fun provideGetUserUseCase(userRepository: UserRepository): GetUserUseCase {
            return GetUserUseCase(userRepository)
        }
    }
}
