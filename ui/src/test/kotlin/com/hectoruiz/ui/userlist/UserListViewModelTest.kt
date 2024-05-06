package com.hectoruiz.ui.userlist

import app.cash.turbine.test
import com.hectoruiz.domain.usecases.DeleteUserUseCase
import com.hectoruiz.domain.usecases.FetchUsersUseCase
import com.hectoruiz.domain.usecases.GetUsersUseCase
import com.hectoruiz.domain.commons.Error
import com.hectoruiz.ui.MainDispatcherRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
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
            coEvery { fetchUsersUseCase.getRemoteUsers(true) } returns Result.failure(Throwable())
            every { getUsersUseCase.getUsers() } returns flowOf(emptyList())

            userListViewModel =
                UserListViewModel(getUsersUseCase, fetchUsersUseCase, deleteUserUseCase)

            val errorJob = launch {
                userListViewModel.error.test {
                    val errorState = awaitItem()
                    assertTrue(errorState is Error.Network)
                    cancelAndConsumeRemainingEvents()
                }
            }
            assertTrue(userListViewModel.uiState.value is UserListUiState.Loading)

            errorJob.join()
            errorJob.cancel()

            assertTrue(userListViewModel.users.first().isEmpty())
            assertTrue(userListViewModel.uiState.value is UserListUiState.NotLoading)
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
                    assertTrue(errorState is Error.Network)
                    cancelAndConsumeRemainingEvents()
                }
            }
            assertTrue(userListViewModel.uiState.value is UserListUiState.Loading)

            errorJob.join()
            errorJob.cancel()

            assertTrue(userListViewModel.users.first().isEmpty())
            assertTrue(userListViewModel.uiState.value is UserListUiState.NotLoading)
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

            assertTrue(userListViewModel.uiState.value is UserListUiState.Loading)

            usersJob.join()
            usersJob.cancel()

            assertTrue(userListViewModel.uiState.value is UserListUiState.NotLoading)
        }
    }

    @Test
    fun `error retrieving more users`() {
        runTest {
            val errorMessage = "Network message"
            coEvery { fetchUsersUseCase.getRemoteUsers(any()) } returns Result.success(Unit) andThen Result.failure(
                Throwable(
                    errorMessage
                )
            )
            every { getUsersUseCase.getUsers() } returns flowOf(listOf(mockk(), mockk(), mockk()))

            userListViewModel =
                UserListViewModel(getUsersUseCase, fetchUsersUseCase, deleteUserUseCase)

            val errorJob = launch {
                userListViewModel.users.test {
                    val emptyUsers = awaitItem()
                    assertTrue(emptyUsers.isEmpty())

                    val users = awaitItem()
                    assertTrue(users.isNotEmpty())
                    assertTrue(users.size == 3)
                }
                userListViewModel.error.test {
                    val errorState = awaitItem()
                    assertTrue(errorState is Error.Network)
                    cancelAndConsumeRemainingEvents()
                }
            }
            assertTrue(userListViewModel.uiState.value is UserListUiState.Loading)

            delay(500L)

            userListViewModel.getMoreUsers()

            assertTrue(userListViewModel.uiState.value is UserListUiState.LoadMore)

            errorJob.join()
            errorJob.cancel()

            assertTrue(userListViewModel.uiState.value is UserListUiState.NotLoading)
        }
    }

    @Test
    fun `success retrieving more users`() {
        runTest {
            coEvery { fetchUsersUseCase.getRemoteUsers(any()) } returns Result.success(Unit)
            every { getUsersUseCase.getUsers() } returns flowOf(
                listOf(
                    mockk(),
                    mockk(),
                    mockk()
                )
            )

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

            assertTrue(userListViewModel.uiState.value is UserListUiState.Loading)

            userListViewModel.getMoreUsers()

            assertTrue(userListViewModel.uiState.value is UserListUiState.LoadMore)

            usersJob.join()
            usersJob.cancel()

            assertTrue(userListViewModel.uiState.value is UserListUiState.NotLoading)
            coVerify { fetchUsersUseCase.getRemoteUsers(false) }
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

            assertTrue(userListViewModel.uiState.value is UserListUiState.Loading)

            val errorJob = launch {
                userListViewModel.error.test {
                    assertEquals(Error.Other, awaitItem())
                    cancelAndConsumeRemainingEvents()
                }
            }

            userListViewModel.deleteUser(mockk())

            errorJob.join()
            errorJob.cancel()

            assertTrue(userListViewModel.uiState.value is UserListUiState.NotLoading)
        }
    }

    @Test
    fun `success deleting user`() {
        runTest {
            coEvery { fetchUsersUseCase.getRemoteUsers(any()) } returns Result.success(Unit)
            coEvery { deleteUserUseCase.deleteUser(any()) } returns true
            every { getUsersUseCase.getUsers() } returns flowOf(
                listOf(
                    mockk(),
                    mockk(),
                    mockk()
                )
            ) andThen flowOf(
                listOf(mockk(), mockk())
            )
            userListViewModel =
                UserListViewModel(getUsersUseCase, fetchUsersUseCase, deleteUserUseCase)

            assertTrue(userListViewModel.uiState.value is UserListUiState.Loading)

            userListViewModel.deleteUser(mockk())

            verify { getUsersUseCase.getUsers() }
        }
    }
}
