package com.hectoruiz.ui.userlist

import app.cash.turbine.test
import com.hectoruiz.domain.usecases.DeleteUserUseCase
import com.hectoruiz.domain.usecases.FetchUsersUseCase
import com.hectoruiz.domain.usecases.GetUsersUseCase
import com.hectoruiz.ui.MainDispatcherRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class UserListViewModelTest {

    init {
        MockKAnnotations.init(this)
    }

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var getUsersUseCase: GetUsersUseCase

    @MockK
    private lateinit var fetchUsersUseCase: FetchUsersUseCase

    @MockK
    private lateinit var deleteUserUseCase: DeleteUserUseCase

    private lateinit var userListViewModel: UserListViewModel

    @Test
    fun `coroutine exception handled`() {
        runTest {
            coEvery { fetchUsersUseCase.getRemoteUsers(4444) } returns Result.failure(Throwable())
            every { getUsersUseCase.getUsers() } returns flowOf(emptyList())

            userListViewModel =
                UserListViewModel(getUsersUseCase, fetchUsersUseCase, deleteUserUseCase)

            val errorJob = launch {
                userListViewModel.error.test {
                    val errorState = awaitItem()
                    assertTrue(errorState is ErrorState.NetworkError)
                    cancelAndConsumeRemainingEvents()
                }
            }
            assertTrue(userListViewModel.loading.value)

            errorJob.join()
            errorJob.cancel()

            assertTrue(userListViewModel.users.first().isEmpty())
        }
    }

    @Test
    fun `error retrieving users`() {
        runTest {
            val errorMessage = "Network message"
            coEvery { fetchUsersUseCase.getRemoteUsers(any()) } returns Result.failure(
                Throwable(
                    errorMessage
                )
            )
            every { getUsersUseCase.getUsers() } returns flowOf(emptyList())

            userListViewModel =
                UserListViewModel(getUsersUseCase, fetchUsersUseCase, deleteUserUseCase)

            val errorJob = launch {
                userListViewModel.error.test {
                    val errorState = awaitItem()
                    assertTrue(errorState is ErrorState.NetworkError)
                    assertEquals(errorMessage, (errorState as ErrorState.NetworkError).message)
                    cancelAndConsumeRemainingEvents()
                }
            }
            assertTrue(userListViewModel.loading.value)

            errorJob.join()
            errorJob.cancel()

            assertTrue(userListViewModel.users.first().isEmpty())
        }
    }

    @Test
    fun `success retrieving users`() {
        runTest {
            coEvery { fetchUsersUseCase.getRemoteUsers(any()) } returns Result.success(Unit)
            every { getUsersUseCase.getUsers() } returns flowOf(listOf(mockk(), mockk(), mockk()))

            val usersJob = launch {
                userListViewModel.users.test {
                    val emptyUsers = awaitItem()
                    assertTrue(emptyUsers.isEmpty())

                    val users = awaitItem()
                    assertTrue(users.isNotEmpty())
                    assertTrue(users.size == 3)
                    cancelAndConsumeRemainingEvents()
                }
            }
            userListViewModel =
                UserListViewModel(getUsersUseCase, fetchUsersUseCase, deleteUserUseCase)

            assertTrue(userListViewModel.loading.value)
            assertTrue(userListViewModel.page.value == 1)

            usersJob.join()
            usersJob.cancel()

            assertFalse(userListViewModel.loading.value)
            assertTrue(userListViewModel.page.value == 2)
        }
    }

    @Test
    fun `error deleting user`() {
        runTest {
            coEvery { fetchUsersUseCase.getRemoteUsers(any()) } returns Result.success(Unit)
            coEvery { deleteUserUseCase.deleteUser(any()) } returns false
            every { getUsersUseCase.getUsers() } returns flowOf(emptyList())
            userListViewModel =
                UserListViewModel(getUsersUseCase, fetchUsersUseCase, deleteUserUseCase)

            assertTrue(userListViewModel.loading.value)

            val errorJob = launch {
                userListViewModel.error.test {
                    assertEquals(ErrorState.Unknown, awaitItem())
                    cancelAndConsumeRemainingEvents()
                }
            }

            userListViewModel.deleteUser(mockk())
            assertTrue(userListViewModel.loading.value)

            errorJob.join()
            errorJob.cancel()

            assertFalse(userListViewModel.loading.value)
        }
    }

    @Test
    fun `success deleting user`() {
        runTest {
            coEvery { fetchUsersUseCase.getRemoteUsers(any()) } returns Result.success(Unit)
            coEvery { deleteUserUseCase.deleteUser(any()) } returns true
            every { getUsersUseCase.getUsers() } returns flowOf(emptyList())
            userListViewModel =
                UserListViewModel(getUsersUseCase, fetchUsersUseCase, deleteUserUseCase)

            assertTrue(userListViewModel.loading.value)

            userListViewModel.deleteUser(mockk())

            delay(500L)

            assertFalse(userListViewModel.loading.value)
        }
    }
}