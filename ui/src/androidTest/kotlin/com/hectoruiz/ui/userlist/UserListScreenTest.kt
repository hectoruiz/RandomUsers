package com.hectoruiz.ui.userlist

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.hectoruiz.domain.models.UserModel
import com.hectoruiz.domain.commons.Error
import com.hectoruiz.ui.R
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserListScreenTest {

    init {
        MockKAnnotations.init(this)
    }

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    private val emptyUsersList = emptyList<UserModel>()

    @RelaxedMockK
    lateinit var user: UserModel
    private lateinit var usersList: List<UserModel>

    @Before
    fun setUp() {
        every { user.email } returns USER_EMAIL
        usersList = listOf(user)
    }

    @Test
    fun showLoaderWhenItsLoading() {
        composeRule.setContent {
            UserListScreen(
                users = emptyUsersList,
                uiState = UserListUiState.Loading,
                error = Error.NoError,
                onUserSearch = {},
                onClickMoreUsers = {},
                onDeleteUser = {},
                navigateToDetail = {}
            )
        }
        composeRule.onNodeWithTag(TAG_LOADING_USERS).assertExists()
    }

    @Test
    fun showLoaderWhenLoadingMoreUsers() {
        composeRule.setContent {
            UserListScreen(
                users = emptyUsersList,
                uiState = UserListUiState.LoadMore,
                error = Error.NoError,
                onUserSearch = {},
                onClickMoreUsers = {},
                onDeleteUser = {},
                navigateToDetail = {}
            )
        }
        composeRule.onNodeWithTag(TAG_LOAD_MORE_USERS).assertExists()
    }

    @Test
    fun hideLoaderWhenItsNotLoading() {
        composeRule.setContent {
            UserListScreen(
                users = emptyUsersList,
                uiState = UserListUiState.NotLoading,
                error = Error.NoError,
                onUserSearch = {},
                onClickMoreUsers = {},
                onDeleteUser = {},
                navigateToDetail = {}
            )
        }
        composeRule.onNodeWithTag(TAG_LOADING_USERS).assertDoesNotExist()
    }

    @Test
    fun emptyUsersNotShowUserList() {
        composeRule.setContent {
            UserListScreen(
                users = emptyUsersList,
                uiState = UserListUiState.NotLoading,
                error = Error.NoError,
                onUserSearch = {},
                onClickMoreUsers = {},
                onDeleteUser = {},
                navigateToDetail = {}
            )
        }
        composeRule.onNodeWithTag(TAG_USER_LIST).assertIsNotDisplayed()
    }

    @Test
    fun usersShowUserList() {
        composeRule.setContent {
            UserListScreen(
                users = usersList,
                uiState = UserListUiState.NotLoading,
                error = Error.NoError,
                onUserSearch = {},
                onClickMoreUsers = {},
                onDeleteUser = {},
                navigateToDetail = {}
            )
        }
        composeRule.onNodeWithTag(TAG_USER_LIST).assertIsDisplayed()
        composeRule.onNodeWithTag(TAG_USER_LIST).performScrollToIndex(usersList.size - 1)
        assertEquals(
            usersList.size,
            composeRule.onAllNodes(hasTestTag(TAG_USER_LIST_ITEM)).fetchSemanticsNodes().size
        )
    }

    @Test
    fun userClickOnUserItem() {
        every { user.email } returns USER_EMAIL
        composeRule.setContent {
            UserListScreen(
                users = usersList,
                uiState = UserListUiState.NotLoading,
                error = Error.NoError,
                onUserSearch = {},
                onClickMoreUsers = {},
                onDeleteUser = {},
                navigateToDetail = { assertEquals(USER_EMAIL, it) }
            )
        }
        composeRule.onNodeWithTag(TAG_USER_LIST).onChildAt(0).performClick()
    }

    @Test
    fun userClickOnMoreUsers() {
        var onMoreUsersClicked = false
        composeRule.setContent {
            UserListScreen(
                users = usersList,
                uiState = UserListUiState.NotLoading,
                error = Error.NoError,
                onUserSearch = {},
                onClickMoreUsers = { onMoreUsersClicked = true },
                onDeleteUser = {},
                navigateToDetail = {}
            )
        }
        composeRule.onNodeWithTag(TAG_USER_LIST).performScrollToIndex(usersList.size - 1)
        composeRule.onNodeWithTag(TAG_MORE_USERS_BUTTON).performClick()
        assertTrue(onMoreUsersClicked)
    }

    @Test
    fun userClickOnDeleteUser() {
        composeRule.setContent {
            UserListScreen(
                users = usersList,
                uiState = UserListUiState.NotLoading,
                error = Error.NoError,
                onUserSearch = {},
                onClickMoreUsers = {},
                onDeleteUser = { assertEquals(user, it) },
                navigateToDetail = {}
            )
        }
        composeRule.onAllNodes(hasTestTag(TAG_USER_DELETE_BUTTON), false)[0].performClick()
    }

    @Test
    fun isShowingNetworkError() {
        val networkMessage = composeRule.activity.getString(R.string.network_error)
        composeRule.setContent {
            UserListScreen(
                users = usersList,
                uiState = UserListUiState.NotLoading,
                error = Error.Network,
                onUserSearch = {},
                onClickMoreUsers = {},
                onDeleteUser = { assertEquals(user, it) },
                navigateToDetail = {}
            )
        }
        composeRule.onNodeWithText(networkMessage).assertIsDisplayed()
    }

    @Test
    fun isShowingError() {
        val defaultMessage = composeRule.activity.getString(R.string.default_error)
        composeRule.setContent {
            UserListScreen(
                users = usersList,
                uiState = UserListUiState.NotLoading,
                error = Error.Other,
                onUserSearch = {},
                onClickMoreUsers = {},
                onDeleteUser = { assertEquals(user, it) },
                navigateToDetail = {}
            )
        }
        composeRule.onNodeWithText(defaultMessage).assertIsDisplayed()
    }

    @Test
    fun isShowingUserItem() {
        every { user.name } returns USER_NAME
        every { user.phone } returns USER_PHONE
        every { user.email } returns USER_EMAIL
        composeRule.setContent {
            UserListScreen(
                users = listOf(user),
                uiState = UserListUiState.NotLoading,
                error = Error.NoError,
                onUserSearch = {},
                onClickMoreUsers = {},
                onDeleteUser = {},
                navigateToDetail = {}
            )
        }
        composeRule.onNodeWithText(USER_NAME).assertIsDisplayed()
        composeRule.onNodeWithText(USER_PHONE).assertIsDisplayed()
        composeRule.onNodeWithText(USER_EMAIL).assertIsDisplayed()
    }

    @Test
    fun isSearchBarNotSearching() {
        composeRule.setContent {
            UserListScreen(
                users = listOf(user),
                uiState = UserListUiState.NotLoading,
                error = Error.NoError,
                onUserSearch = {},
                onClickMoreUsers = {},
                onDeleteUser = {},
                navigateToDetail = {}
            )
        }
        composeRule.onNodeWithTag(TAG_USER_SEARCH).performTextInput(KEY_WORD)
        composeRule.onNodeWithTag(TAG_CLEAR_SEARCH).assertIsDisplayed()
    }

    @Test
    fun isSearchBarSearching() {
        composeRule.setContent {
            UserListScreen(
                users = listOf(user),
                uiState = UserListUiState.NotLoading,
                error = Error.NoError,
                onUserSearch = {},
                onClickMoreUsers = {},
                onDeleteUser = {},
                navigateToDetail = {}
            )
        }
        composeRule.onNodeWithTag(TAG_USER_SEARCH).performTextInput("")
        composeRule.onNodeWithTag(TAG_CLEAR_SEARCH).assertIsNotDisplayed()
    }

    private companion object {
        const val USER_NAME = "User Name"
        const val USER_PHONE = "613212112"
        const val USER_EMAIL = "user@mail.com"
        const val KEY_WORD = "ta"
    }
}
