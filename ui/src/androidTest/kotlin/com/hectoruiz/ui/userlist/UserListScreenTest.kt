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
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.hectoruiz.domain.models.UserModel
import com.hectoruiz.ui.R
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
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
    val rule = createAndroidComposeRule<ComponentActivity>()

    private val emptyUsersList = emptyList<UserModel>()

    @RelaxedMockK
    lateinit var user: UserModel
    private lateinit var usersList: List<UserModel>

    @Before
    fun setUp() {
        every { user.name } returns USER_NAME
        every { user.id } returns USER_ID
        usersList = listOf(user, user, user, user, user)
    }

    @Test
    fun showLoaderWhenItsLoading() {
        rule.setContent {
            UserListScreen(
                users = emptyUsersList,
                loading = true,
                error = ErrorState.NoError,
                onClickMoreImages = {},
                onDeleteUser = {},
                navigateToDetail = {}
            )
        }
        rule.onNodeWithTag(TAG_USER_LIST_CIRCULAR_PROGRESS_INDICATOR).assertExists()
    }

    @Test
    fun hideLoaderWhenItsNotLoading() {
        rule.setContent {
            UserListScreen(
                users = emptyUsersList,
                loading = false,
                error = ErrorState.NoError,
                onClickMoreImages = {},
                onDeleteUser = {},
                navigateToDetail = {}
            )
        }
        rule.onNodeWithTag(TAG_USER_LIST_CIRCULAR_PROGRESS_INDICATOR).assertDoesNotExist()
    }

    @Test
    fun emptyUsersNotShowUserList() {
        rule.setContent {
            UserListScreen(
                users = emptyUsersList,
                loading = false,
                error = ErrorState.NoError,
                onClickMoreImages = {},
                onDeleteUser = {},
                navigateToDetail = {}
            )
        }
        rule.onNodeWithTag(TAG_USER_LIST).assertIsNotDisplayed()
    }

    @Test
    fun usersShowUserList() {
        rule.setContent {
            UserListScreen(
                users = usersList,
                loading = false,
                error = ErrorState.NoError,
                onClickMoreImages = {},
                onDeleteUser = {},
                navigateToDetail = {}
            )
        }
        rule.onNodeWithTag(TAG_USER_LIST).assertIsDisplayed()
        rule.onNodeWithTag(TAG_USER_LIST).performScrollToIndex(usersList.size - 1)
        assertEquals(
            usersList.size,
            rule.onAllNodes(hasTestTag(TAG_USER_LIST_ITEM)).fetchSemanticsNodes().size
        )
    }

    @Test
    fun userClickOnUserItem() {
        rule.setContent {
            UserListScreen(
                users = usersList,
                loading = false,
                error = ErrorState.NoError,
                onClickMoreImages = {},
                onDeleteUser = {},
                navigateToDetail = { assertEquals("1234", it) }
            )
        }
        rule.onNodeWithTag(TAG_USER_LIST).onChildAt(0).performClick()
    }

    @Test
    fun userClickOnMoreImages() {
        var wasCalled = false
        rule.setContent {
            UserListScreen(
                users = usersList,
                loading = false,
                error = ErrorState.NoError,
                onClickMoreImages = { wasCalled = true },
                onDeleteUser = {},
                navigateToDetail = {}
            )
        }
        rule.onNodeWithTag(TAG_MORE_IMAGES_BUTTON).performClick()
        assertTrue(wasCalled)
    }

    @Test
    fun userClickOnDeleteUser() {
        rule.setContent {
            UserListScreen(
                users = usersList,
                loading = false,
                error = ErrorState.NoError,
                onClickMoreImages = {},
                onDeleteUser = { assertEquals(user, it) },
                navigateToDetail = {}
            )
        }
        rule.onAllNodes(hasTestTag(TAG_USER_DELETE_BUTTON), false)[0].performClick()
    }

    @Test
    fun isShowingNetworkError() {
        val networkMessage = "Network error"
        val networkError = ErrorState.NetworkError(networkMessage)
        rule.setContent {
            UserListScreen(
                users = usersList,
                loading = false,
                error = networkError,
                onClickMoreImages = {},
                onDeleteUser = { assertEquals(user, it) },
                navigateToDetail = {}
            )
        }
        rule.onNodeWithText(networkMessage).assertIsDisplayed()
    }

    @Test
    fun isShowingUnknownError() {
        rule.setContent {
            UserListScreen(
                users = usersList,
                loading = false,
                error = ErrorState.Unknown,
                onClickMoreImages = {},
                onDeleteUser = { assertEquals(user, it) },
                navigateToDetail = {}
            )
        }
        val unknownErrorMessage = rule.activity.getString(R.string.default_error)
        rule.onNodeWithText(unknownErrorMessage).assertIsDisplayed()
    }

    @Test
    fun isShowingUserItem() {
        every { user.phone } returns USER_PHONE
        every { user.email } returns USER_EMAIL
        rule.setContent {
            UserListScreen(
                users = listOf(user),
                loading = false,
                error = ErrorState.Unknown,
                onClickMoreImages = {},
                onDeleteUser = {},
                navigateToDetail = {}
            )
        }
        rule.onNodeWithText(USER_NAME).assertIsDisplayed()
        rule.onNodeWithText(USER_PHONE).assertIsDisplayed()
        rule.onNodeWithText(USER_EMAIL).assertIsDisplayed()
    }

    private companion object {
        const val USER_ID = "1234"
        const val USER_NAME = "User Name"
        const val USER_PHONE = "613212112"
        const val USER_EMAIL = "user@mail.com"
    }
}
