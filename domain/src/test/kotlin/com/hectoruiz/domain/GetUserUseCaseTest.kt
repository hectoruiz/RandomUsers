package com.hectoruiz.domain

import com.hectoruiz.domain.models.Gender
import com.hectoruiz.domain.models.UserModel
import com.hectoruiz.domain.repositories.UserRepository
import com.hectoruiz.domain.usecases.GetUserUseCase
import com.hectoruiz.domain.usecases.GetUserUseCase.Companion.PARSING_ERROR
import com.hectoruiz.domain.usecases.transformDate
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GetUserUseCaseTest {

    init {
        MockKAnnotations.init(this)
    }

    @MockK
    private lateinit var userRepository: UserRepository

    @MockK
    private lateinit var userModel: UserModel

    private val getUserUseCase by lazy { GetUserUseCase(userRepository) }

    @Test
    fun `error fetching users from remote`() {
        val errorMessage = "User not found"
        val errorResponse = flowOf(Result.failure<UserModel>(Throwable(errorMessage)))
        coEvery { userRepository.getUser(EMAIL) } returns errorResponse

        val result = runBlocking { getUserUseCase.getUser(EMAIL).first() }

        assertTrue(result.isFailure)
        assertEquals(errorMessage, result.exceptionOrNull()?.message)
    }

    @Test
    fun `success fetching users from remote`() {
        val registeredDate = "1992-03-08T15:13:16.688Z"
        val successResponse = flowOf(
            Result.success(
                UserModel(
                    gender = Gender.UNSPECIFIED,
                    name = "user name",
                    email = "user email",
                    thumbnail = "user thumbnail",
                    picture = "user picture",
                    phone = "user phone",
                    address = "user address",
                    location = "user location",
                    registeredDate = registeredDate,
                    isActive = false,
                )
            )
        )
        coEvery { userRepository.getUser(EMAIL) } returns successResponse

        val result = runBlocking { getUserUseCase.getUser(EMAIL).first() }

        assertTrue(result.isSuccess)
        result.onSuccess {
            assertEquals(registeredDate.transformDate(), it.registeredDate)
        }
    }

    @Test
    fun `success fetching users from remote with invalid date`() {
        val registeredDate = "05-04-2004"
        every { userModel.registeredDate } returns registeredDate
        val successResponse = flowOf(Result.success(userModel))
        coEvery { userRepository.getUser(EMAIL) } returns successResponse

        val result = runBlocking { getUserUseCase.getUser(EMAIL).first() }

        assertTrue(result.isFailure)
        assertEquals(PARSING_ERROR, result.exceptionOrNull()?.message)
    }

    private companion object {
        const val EMAIL = "email"
    }
}
