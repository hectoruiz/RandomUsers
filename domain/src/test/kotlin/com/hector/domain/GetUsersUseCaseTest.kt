package com.hector.domain

import com.hectoruiz.domain.models.UserModel
import com.hectoruiz.domain.repositories.UserRepository
import com.hectoruiz.domain.usecases.GetUsersUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GetUsersUseCaseTest {

    init {
        MockKAnnotations.init(this)
    }

    @MockK
    private lateinit var userRepository: UserRepository

    private val getUsersUseCase by lazy { GetUsersUseCase(userRepository) }

    @Test
    fun `error retrieving users`() {
        coEvery { userRepository.getUsers() } returns flowOf(emptyList())

        val result = runBlocking { getUsersUseCase.getUsers().first() }
        assertTrue(result.isEmpty())
    }

    @Test
    fun `success retrieving users`() {
        val userModelActive = mockk<UserModel>(relaxed = true)
        every { userModelActive.isActive } returns true
        val userModelNonActive = mockk<UserModel>(relaxed = true)
        every { userModelNonActive.isActive } returns false

        val usersList = listOf(userModelActive, userModelNonActive, userModelActive)
        coEvery { userRepository.getUsers() } returns flowOf(usersList)

        val result = runBlocking { getUsersUseCase.getUsers().first() }
        assertEquals(2, result.size)
    }
}