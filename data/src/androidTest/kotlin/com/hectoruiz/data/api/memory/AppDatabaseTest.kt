package com.hectoruiz.data.api.memory

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.hectoruiz.data.entities.UserEntity
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class AppDatabaseTest {
    private lateinit var userDao: UserDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).build()
        userDao = db.userDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertUsersInDatabase() {
        val user1 = mockk<UserEntity>(relaxed = true)
        every { user1.email } returns "email1"
        val user2 = mockk<UserEntity>(relaxed = true)
        every { user2.email } returns "email2"
        val users = listOf(user1, user2)
        runBlocking { userDao.insertAll(users) }

        val amountUsers = runBlocking { userDao.getNumUsers() }
        assertEquals(users.size, amountUsers)

        val result = runBlocking {
            userDao.getAll().first()
        }
        result.mapIndexed { index, userEntity ->
            assertEquals(userEntity.email, users[index].email)
        }
    }

    @Test
    fun getUserInDatabase() {
        val user1 = mockk<UserEntity>(relaxed = true)
        every { user1.email } returns "email1"
        val user2 = mockk<UserEntity>(relaxed = true)
        every { user2.email } returns "email2"
        val users = listOf(user1, user2)
        runBlocking { userDao.insertAll(users) }

        val user = runBlocking { userDao.getUser("email1").first() }
        assertEquals(user1.email, user?.email)
    }

    @Test
    fun updateUser() {
        val user = mockk<UserEntity>(relaxed = true)
        every { user.email } returns "email1"
        every { user.isActive } returns true
        runBlocking { userDao.insertAll(listOf(user)) }

        val beforeUpdate = runBlocking { userDao.getUser("email1").first() }
        assertTrue(beforeUpdate?.isActive == true)

        every { user.isActive } returns false
        runBlocking { userDao.update(user) }

        val afterUpdate = runBlocking { userDao.getUser("email1").first() }
        assertTrue(afterUpdate?.isActive == false)
    }
}
