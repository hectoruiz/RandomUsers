package com.hectoruiz.data.repositories

import com.hectoruiz.data.entities.UserEntity
import com.hectoruiz.data.entities.toModel
import com.hectoruiz.data.models.UsersApiModel
import com.hectoruiz.domain.Constants
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class UserRepositoryImplTest {

    init {
        MockKAnnotations.init(this)
    }

    @MockK
    private lateinit var remoteDataSource: UserRemoteDataSource

    @MockK
    private lateinit var memoryDataSource: UserMemoryDataSource

    private val userRepository by lazy { UserRepositoryImpl(remoteDataSource, memoryDataSource) }

    @Test
    fun `empty get users`() {
        coEvery { memoryDataSource.getUsers() } returns flowOf(emptyList())

        val result = runBlocking { userRepository.getUsers().first() }

        assertTrue(result.isEmpty())
    }

    @Test
    fun `success get users`() {
        val userListEntity = listOf<UserEntity>(mockk(relaxed = true))
        coEvery { memoryDataSource.getUsers() } returns flowOf(userListEntity)

        val result = runBlocking { userRepository.getUsers().first() }

        assertTrue(result.isNotEmpty())
        assertEquals(userListEntity.toModel(), result)
    }

    @Test
    fun `error fetch users`() {
        val errorMessage = "Error getting users"
        val errorResponse = Result.failure<UsersApiModel>(Throwable(errorMessage))
        coEvery { remoteDataSource.getUsers(1) } returns errorResponse

        val result = runBlocking { userRepository.getRemoteUsers(1) }

        assertTrue(result.isFailure)
        result.onFailure {
            assertEquals(errorMessage, it.message)
        }
    }

    @Test
    fun `success fetch users`() {
        val successResponse = Result.success(UsersApiModel())
        coEvery { remoteDataSource.getUsers(1) } returns successResponse
        coEvery { memoryDataSource.addUsers(any()) } returns Unit

        val result = runBlocking { userRepository.getRemoteUsers(1) }

        assertTrue(result.isSuccess)
    }

    @Test
    fun `error delete user`() {
        coEvery { memoryDataSource.deleteUser(any()) } returns false

        val result = runBlocking { userRepository.deleteUser(mockk(relaxed = true)) }

        assertFalse(result)
    }

    @Test
    fun `success delete user`() {
        coEvery { memoryDataSource.deleteUser(any()) } returns true

        val result = runBlocking { userRepository.deleteUser(mockk(relaxed = true)) }

        assertTrue(result)
    }

    @Test
    fun `error get specific user`() {
        coEvery { memoryDataSource.getUser("1") } returns flowOf(null)

        val result = runBlocking { userRepository.getUser("1").first() }

        assertTrue(result.isFailure)
        result.onFailure {
            assertEquals(Constants.USER_NOT_FOUND, it.message)
        }
    }

    @Test
    fun `success get specific user`() {
        val userEntity = mockk<UserEntity>(relaxed = true)
        coEvery { memoryDataSource.getUser("1") } returns flowOf(userEntity)

        val result = runBlocking { userRepository.getUser("1").first() }

        assertTrue(result.isSuccess)
        result.onSuccess {
            assertEquals(userEntity.toModel(), it)
        }
    }
}