package com.hectoruiz.domain

import com.hectoruiz.domain.repositories.UserRepository
import com.hectoruiz.domain.usecases.FetchUsersUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FetchUsersUseCaseTest {

    init {
        MockKAnnotations.init(this)
    }

    @MockK
    private lateinit var userRepository: UserRepository

    private val fetchUsersUseCase by lazy { FetchUsersUseCase(userRepository) }

    @Test
    fun `error fetching users from remote`() {
        val errorMessage = "Error!!"
        val errorResponse = Result.failure<Unit>(Throwable(errorMessage))
        coEvery { userRepository.getRemoteUsers(any()) } returns errorResponse
        coEvery { userRepository.getAmountUsers() } returns 0

        val result = runBlocking { fetchUsersUseCase.getRemoteUsers(false) }

        assertTrue(result.isFailure)
        assertEquals(errorMessage, result.exceptionOrNull()?.message)
    }

    @Test
    fun `success fetching users from remote`() {
        coEvery { userRepository.getRemoteUsers(any()) } returns Result.success(Unit)
        coEvery { userRepository.getAmountUsers() } returns 0


        val result = runBlocking { fetchUsersUseCase.getRemoteUsers(true) }
        assertTrue(result.isSuccess)
    }

    @Test
    fun `not needed to fetch users from remote`() {
        coEvery { userRepository.getAmountUsers() } returns 20
        coEvery { userRepository.getRemoteUsers(any()) } returns Result.success(Unit)


        val result = runBlocking { fetchUsersUseCase.getRemoteUsers(true, 20) }
        assertTrue(result.isSuccess)
    }
}
