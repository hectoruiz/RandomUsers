package com.hectoruiz.randomusers.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.IntOffset
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.hectoruiz.domain.commons.Error
import com.hectoruiz.ui.userdetail.UserDetailScreen
import com.hectoruiz.ui.userdetail.UserDetailViewModel
import com.hectoruiz.ui.userlist.UserListScreen
import com.hectoruiz.ui.userlist.UserListViewModel

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = START_DESTINATION) {
        composable(
            route = START_DESTINATION,
            enterTransition = {
                slideIn(initialOffset = { IntOffset(300, 200) }) + fadeIn(
                    tween(
                        ANIMATION_LIST_LENGTH
                    )
                )
            },
            popEnterTransition = {
                slideIn(initialOffset = { IntOffset(300, 200) }) + fadeIn(
                    tween(
                        ANIMATION_LIST_LENGTH
                    )
                )
            },
            exitTransition = {
                slideOut(targetOffset = { IntOffset(300, 200) }) + fadeOut(
                    tween(
                        ANIMATION_LIST_LENGTH
                    )
                )
            },
            popExitTransition = {
                slideOut(targetOffset = { IntOffset(300, 200) }) + fadeOut(
                    tween(
                        ANIMATION_LIST_LENGTH
                    )
                )
            }
        ) {
            val userListViewModel = hiltViewModel<UserListViewModel>()
            val uiState by userListViewModel.uiState.collectAsStateWithLifecycle()
            val users by userListViewModel.users.collectAsStateWithLifecycle(emptyList())
            val error by userListViewModel.error.collectAsStateWithLifecycle(Error.NoError)

            UserListScreen(
                users = users,
                uiState = uiState,
                error = error,
                onUserSearch = { userListViewModel.searchUsers(it) },
                onClickMoreUsers = { userListViewModel.getMoreUsers() },
                onDeleteUser = { userListViewModel.deleteUser(it) },
                navigateToDetail = {
                    navController.navigate("$FINAL_DESTINATION/$it")
                },
            )
        }
        composable(
            route = "$FINAL_DESTINATION/{$PARAM}",
            arguments = listOf(navArgument(PARAM) { type = NavType.StringType }),
            enterTransition = {
                slideIn(initialOffset = { IntOffset(300, 200) }) + fadeIn(
                    tween(
                        ANIMATION_DETAIL_LENGTH
                    )
                )
            },
            popEnterTransition = {
                slideIn(initialOffset = { IntOffset(300, 200) }) + fadeIn(
                    tween(
                        ANIMATION_DETAIL_LENGTH
                    )
                )
            },
            exitTransition = {
                slideOut(targetOffset = { IntOffset(300, 200) }) + fadeOut(
                    tween(
                        ANIMATION_DETAIL_LENGTH
                    )
                )
            },
            popExitTransition = {
                slideOut(targetOffset = { IntOffset(300, 200) }) + fadeOut(
                    tween(
                        ANIMATION_DETAIL_LENGTH
                    )
                )
            }
        ) {
            val userDetailViewModel = hiltViewModel<UserDetailViewModel>()
            val userDetailUiState by userDetailViewModel.userDetailUiState.collectAsStateWithLifecycle()

            UserDetailScreen(userDetailUiState = userDetailUiState) { navController.popBackStack() }
        }
    }
}

const val START_DESTINATION = "userList"
const val FINAL_DESTINATION = "userDetail"
const val PARAM = "email"
private const val ANIMATION_LIST_LENGTH = 1000
private const val ANIMATION_DETAIL_LENGTH = 500
