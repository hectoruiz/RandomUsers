package com.hectoruiz.randomusers

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.hectoruiz.randomusers.navigation.NavigationGraph
import com.hectoruiz.randomusers.navigation.START_DESTINATION
import com.hectoruiz.ui.userdetail.TAG_USER_DETAIL_BACK
import com.hectoruiz.ui.userlist.TAG_USER_LIST
import com.hectoruiz.ui.userlist.TAG_USER_LIST_ITEM
import com.hectoruiz.ui.userlist.TAG_USER_SEARCH
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class NavigationTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivityTest>()
    private lateinit var navController: TestNavHostController

    @Before
    fun setUp() {
        composeRule.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            NavigationGraph(navController = navController)
        }
    }

    @Test
    fun initialScreen() {
        composeRule.onNodeWithTag(TAG_USER_SEARCH).assertIsDisplayed()
        assertEquals(START_DESTINATION, navController.graph.startDestDisplayName)
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun navigateToDetail() {
        composeRule.waitUntil(5000L) {
            composeRule
                .onAllNodes(hasTestTag(TAG_USER_LIST_ITEM))
                .fetchSemanticsNodes().size > 1
        }
        composeRule.onNodeWithTag(TAG_USER_LIST).onChildAt(0).performClick()
        composeRule.waitUntilExactlyOneExists(hasTestTag(TAG_USER_DETAIL_BACK), 5000L)
        composeRule.onNodeWithTag(TAG_USER_DETAIL_BACK).assertIsDisplayed()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun detailNavigateToList() {
        composeRule.waitUntil(5000L) {
            composeRule
                .onAllNodes(hasTestTag(TAG_USER_LIST_ITEM))
                .fetchSemanticsNodes().size > 1
        }
        composeRule.onNodeWithTag(TAG_USER_LIST).onChildAt(0).performClick()

        composeRule.waitUntilExactlyOneExists(hasTestTag(TAG_USER_DETAIL_BACK), 5000L)
        composeRule.onNodeWithTag(TAG_USER_DETAIL_BACK).performClick()

        composeRule.waitUntilExactlyOneExists(hasTestTag(TAG_USER_LIST), 5000L)
        composeRule.onNodeWithTag(TAG_USER_SEARCH).assertIsDisplayed()
    }
}
