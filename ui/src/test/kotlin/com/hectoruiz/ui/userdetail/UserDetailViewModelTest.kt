package com.hectoruiz.ui.userdetail

import androidx.lifecycle.SavedStateHandle
import com.hectoruiz.domain.models.Gender
import com.hectoruiz.domain.models.UserModel
import com.hectoruiz.domain.usecases.GetUserUseCase
import com.hectoruiz.ui.MainDispatcherRule
import com.hectoruiz.ui.userlist.ErrorState
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
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
        every { savedStateHandle.get<String>("userId") } returns "1"
    }

    @Test
    fun `error retrieving specific user`() {
        runTest {
            coEvery { getUserUseCase.getUser("1") } returns flowOf(Result.failure(Throwable()))

            userDetailViewModel = UserDetailViewModel(savedStateHandle, getUserUseCase)

            userDetailViewModel.userDetailUiState.value.apply {
                assertTrue(this.loading)
                assertEquals(
                    UserModel(
                        gender = Gender.UNSPECIFIED,
                        id = "",
                        name = "",
                        email = "",
                        phone = "",
                        picture = "",
                        thumbnail = "",
                        registeredDate = "",
                        address = "",
                        location = "",
                        isActive = false
                    ), this.user
                )
                assertEquals(ErrorState.NoError, this.error)
            }

            delay(200L)

            userDetailViewModel.userDetailUiState.value.apply {
                assertFalse(this.loading)
                assertEquals(user, this.user)
                assertEquals(ErrorState.Unknown, this.error)
            }
        }
    }

    @Test
    fun `success retrieving specific user`() {
        runTest {
            val user = mockk<UserModel>()
            coEvery { getUserUseCase.getUser("1") } returns flowOf(Result.success(user))

            userDetailViewModel = UserDetailViewModel(savedStateHandle, getUserUseCase)

            userDetailViewModel.userDetailUiState.value.apply {
                assertTrue(this.loading)
                assertEquals(
                    UserModel(
                        gender = Gender.UNSPECIFIED,
                        id = "",
                        name = "",
                        email = "",
                        phone = "",
                        picture = "",
                        thumbnail = "",
                        registeredDate = "",
                        address = "",
                        location = "",
                        isActive = false
                    ), this.user
                )
                assertEquals(ErrorState.NoError, this.error)
            }

            delay(200L)

            userDetailViewModel.userDetailUiState.value.apply {
                assertFalse(this.loading)
                assertEquals(user, this.user)
                assertEquals(ErrorState.NoError, this.error)
            }
        }
    }
}