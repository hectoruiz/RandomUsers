package com.hector.domain

import com.hectoruiz.domain.models.UserModel
import com.hectoruiz.domain.repositories.UserRepository
import com.hectoruiz.domain.usecases.DeleteUserUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DeleteUserUseCaseTest {

    init {
        MockKAnnotations.init(this)
    }

    @MockK
    private lateinit var userRepository: UserRepository

    @MockK
    private lateinit var userModel: UserModel

    private val deleteUserUseCase by lazy { DeleteUserUseCase(userRepository) }


    @Test
    fun `error deleting user`() {
        coEvery { userRepository.deleteUser(userModel) } returns false

        val result = runBlocking { deleteUserUseCase.deleteUser(userModel) }
        assertFalse(result)
    }

    @Test
    fun `success deleting user`() {
        coEvery { userRepository.deleteUser(userModel) } returns true

        val result = runBlocking { deleteUserUseCase.deleteUser(userModel) }
        assertTrue(result)
    }
}