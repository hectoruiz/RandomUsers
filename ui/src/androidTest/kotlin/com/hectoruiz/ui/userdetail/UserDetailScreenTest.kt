package com.hectoruiz.ui.userdetail

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.hectoruiz.domain.models.Gender
import com.hectoruiz.domain.models.UserModel
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserDetailScreenTest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    private val user = UserModel(
        gender = Gender.MALE,
        id = "1",
        name = USER_DETAIL_NAME,
        email = USER_DETAIL_EMAIL,
        thumbnail = "",
        picture = "",
        phone = "644992298",
        address = USER_DETAIL_ADDRESS,
        location = USER_DETAIL_LOCATION,
        registeredDate = USER_DETAIL_REGISTERED,
    )
    private var userDetailUiState = UserDetailUiState(user = user)

    @Test
    fun showLoaderWhenItsLoading() {
        userDetailUiState = userDetailUiState.copy(loading = true)
        rule.setContent { UserDetailScreen(userDetailUiState) }
        rule.onNodeWithTag(TAG_USER_DETAIL_CIRCULAR_PROGRESS_INDICATOR).assertExists()
    }

    @Test
    fun hideLoaderWhenItsNotLoading() {
        userDetailUiState = userDetailUiState.copy(loading = false)
        rule.setContent { UserDetailScreen(userDetailUiState) }
        rule.onNodeWithTag(TAG_USER_DETAIL_CIRCULAR_PROGRESS_INDICATOR).assertDoesNotExist()
    }

    @Test
    fun isShowingUserDetail() {
        rule.setContent { UserDetailScreen(userDetailUiState) }

        rule.onNodeWithText(USER_DETAIL_NAME).assertIsDisplayed()
        rule.onNodeWithText(USER_DETAIL_EMAIL).assertIsDisplayed()
        rule.onNodeWithText(USER_DETAIL_ADDRESS).assertIsDisplayed()
        rule.onNodeWithText(USER_DETAIL_LOCATION).assertIsDisplayed()
        rule.onNodeWithText(USER_DETAIL_REGISTERED).assertIsDisplayed()
    }

    private companion object {
        const val USER_DETAIL_NAME = "Chris Brown"
        const val USER_DETAIL_EMAIL = "chrisbrown@gmail.com"
        const val USER_DETAIL_ADDRESS = "Oxford Street 33"
        const val USER_DETAIL_LOCATION = "Oxford Long River United Kingdom Long Long loong"
        const val USER_DETAIL_REGISTERED = "20-04-2009"
    }
}
