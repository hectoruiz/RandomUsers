package com.hectoruiz.data.datasources.memory

import com.hectoruiz.data.api.memory.UserDao
import com.hectoruiz.data.entities.UserEntity
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class UserMemoryDataSourceImplTest {

    init {
        MockKAnnotations.init(this)
    }

    @MockK
    private lateinit var userDao: UserDao

    private val userMemoryDataSource by lazy { UserMemoryDataSourceImpl(userDao) }

    @Test
    fun `empty users list from the database`() {
        coEvery { userDao.getAll() } returns flowOf(emptyList())

        val result = runBlocking { userMemoryDataSource.getUsers().first() }

        assertTrue(result.isEmpty())
    }

    @Test
    fun `users list from the database`() {
        coEvery { userDao.getAll() } returns flowOf(listOf(mockk(), mockk(), mockk(), mockk()))

        val result = runBlocking { userMemoryDataSource.getUsers().first() }

        assertTrue(result.size == 4)
    }

    @Test
    fun `error deleteUser in database`() {
        coEvery { userDao.update(any()) } returns 0

        val result = runBlocking { userMemoryDataSource.deleteUser(mockk(relaxed = true)) }

        assertFalse(result)
    }

    @Test
    fun `success deleteUser in database`() {
        coEvery { userDao.update(any()) } returns 1

        val result = runBlocking { userMemoryDataSource.deleteUser(mockk(relaxed = true)) }

        assertTrue(result)
    }

    @Test
    fun `error get specific user from database`() {
        val userNotFound = null
        coEvery { userDao.getUser("1") } returns flowOf(userNotFound)

        val result = runBlocking { userMemoryDataSource.getUser("1").first() }

        assertNull(result)
    }

    @Test
    fun `success get specific user from database`() {
        val userId = "1"
        val user = UserEntity(userId)
        coEvery { userDao.getUser(userId) } returns flowOf(user)

        val result = runBlocking { userMemoryDataSource.getUser(userId).first() }

        assertNotNull(result)
        assertEquals(userId, result?.id)
    }
}