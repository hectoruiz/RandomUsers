package com.hectoruiz.ui.userdetail

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.hectoruiz.domain.models.UserModel
import com.hectoruiz.domain.usecases.GetUserUseCase
import com.hectoruiz.domain.commons.Error
import com.hectoruiz.ui.MainDispatcherRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UserDetailViewModelTest {

    init {
        MockKAnnotations.init(this)
    }

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var savedStateHandle: SavedStateHandle

    @MockK
    private lateinit var getUserUseCase: GetUserUseCase

    private lateinit var userDetailViewModel: UserDetailViewModel

    @Before
    fun setUp() {
        every { savedStateHandle.get<String>(PARAM_KEY) } returns USER_EMAIL
    }

    @Test
    fun `error retrieving specific user`() {
        runTest {
            coEvery { getUserUseCase.getUser(USER_EMAIL) } returns flowOf(Result.failure(Throwable()))

            userDetailViewModel = UserDetailViewModel(savedStateHandle, getUserUseCase)

            assertTrue(userDetailViewModel.userDetailUiState.value.loading)

            val useDetailJob = launch {
                userDetailViewModel.userDetailUiState.test {
                    val uiState = awaitItem()
                    assertFalse(uiState.loading)
                    assertEquals("", uiState.user.email)
                    assertEquals(Error.Other, uiState.error)
                    cancelAndConsumeRemainingEvents()
                }
            }
            useDetailJob.join()
            useDetailJob.cancel()
        }
    }

    @Test
    fun `success retrieving specific user`() {
        runTest {
            val user = mockk<UserModel>()
            every { user.email } returns USER_EMAIL
            val result = Result.success(user)

            coEvery { getUserUseCase.getUser(USER_EMAIL) } returns flowOf(result)

            userDetailViewModel = UserDetailViewModel(savedStateHandle, getUserUseCase)

            assertTrue(userDetailViewModel.userDetailUiState.value.loading)

            val useDetailJob = launch {
                userDetailViewModel.userDetailUiState.test {
                    val uiState = awaitItem()
                    assertFalse(uiState.loading)
                    assertEquals(USER_EMAIL, uiState.user.email)
                    cancelAndConsumeRemainingEvents()
                }
            }
            useDetailJob.join()
            useDetailJob.cancel()
        }
    }
}

private const val USER_EMAIL = "user@email.com"
