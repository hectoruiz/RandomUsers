package com.hectoruiz.randomusers.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.hectoruiz.ui.userdetail.UserDetailScreen
import com.hectoruiz.ui.userdetail.UserDetailViewModel
import com.hectoruiz.ui.userlist.ErrorState
import com.hectoruiz.ui.userlist.UserListScreen
import com.hectoruiz.ui.userlist.UserListViewModel

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = START_DESTINATION) {
        composable(route = START_DESTINATION) {
            val userListViewModel = hiltViewModel<UserListViewModel>()
            val loading by userListViewModel.loading.collectAsStateWithLifecycle()
            val users by userListViewModel.users.collectAsStateWithLifecycle()
            val error by userListViewModel.error.collectAsStateWithLifecycle(ErrorState.NoError)

            UserListScreen(
                users = users,
                loading = loading,
                error = error,
                onClickMoreImages = { userListViewModel.fetchUsers() },
                onDeleteUser = { userListViewModel.deleteUser(it) },
                navigateToDetail = {
                    navController.navigate("$FINAL_DESTINATION/$it")
                }
            )
        }
        composable(
            route = "$FINAL_DESTINATION/{$PARAM}",
            arguments = listOf(navArgument(PARAM) { type = NavType.StringType })
        ) {
            val userDetailViewModel = hiltViewModel<UserDetailViewModel>()
            val userDetailUiState by userDetailViewModel.userDetailUiState.collectAsStateWithLifecycle()

            UserDetailScreen(userDetailUiState = userDetailUiState)
        }
    }
}

private const val START_DESTINATION = "userList"
private const val FINAL_DESTINATION = "userDetail"
const val PARAM = "userId"
