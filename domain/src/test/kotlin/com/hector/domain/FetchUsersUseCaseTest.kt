package com.hector.domain

import com.hectoruiz.domain.repositories.UserRepository
import com.hectoruiz.domain.usecases.FetchUsersUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FetchUsersUseCaseTest {

    init {
        MockKAnnotations.init(this)
    }

    @RelaxedMockK
    private lateinit var userRepository: UserRepository

    private val fetchUsersUseCase by lazy { FetchUsersUseCase(userRepository) }

    @Test
    fun `error fetching users from remote`() {
        val errorMessage = "Error!!"
        val errorResponse = Result.failure<Unit>(Throwable(errorMessage))
        coEvery { userRepository.getRemoteUsers(1) } returns errorResponse

        val result = runBlocking { fetchUsersUseCase.getRemoteUsers(1) }
        assertTrue(result.isFailure)
        assertEquals(errorMessage, result.exceptionOrNull()?.message)
    }

    @Test
    fun `success fetching users from remote`() {
        val successResponse = Result.success(Unit)
        coEvery { userRepository.getRemoteUsers(1) } returns successResponse

        val result = runBlocking { fetchUsersUseCase.getRemoteUsers(1) }
        assertTrue(result.isSuccess)
        assertEquals(successResponse, result)
    }
}