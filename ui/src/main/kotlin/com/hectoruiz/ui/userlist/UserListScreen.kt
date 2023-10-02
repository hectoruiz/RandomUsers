package com.hectoruiz.ui.userlist

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.hectoruiz.domain.models.Gender
import com.hectoruiz.domain.models.UserModel
import com.hectoruiz.ui.R
import com.hectoruiz.ui.userlist.theme.RandomUsersTheme
import kotlinx.coroutines.launch

private val usersPreview = listOf(
    UserModel(
        gender = Gender.MALE,
        id = "1",
        name = "Chris Brown",
        email = "chrisbrown@gmail.com",
        thumbnail = "",
        picture = "",
        phone = "644992298",
        address = "Oxford Street 33",
        location = "Oxford Long River United Kingdom",
        registeredDate = "20-04-2009",
    ),
    UserModel(
        gender = Gender.FEMALE,
        id = "2",
        name = "Christina Brown",
        email = "christinabrown@gmail.com",
        thumbnail = "",
        picture = "",
        phone = "644992298",
        address = "Oxford Street 33",
        location = "Oxford Long River United Kingdom",
        registeredDate = "20-04-2009",
    )
)

@Preview(showBackground = true)
@Composable
fun UserListScreenPreview() {
    RandomUsersTheme {
        UserListScreen(
            users = usersPreview,
            loading = false,
            error = ErrorState.NoError,
            onClickMoreImages = {},
            onDeleteUser = {},
            navigateToDetail = {},
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun UserListScreen(
    users: List<UserModel>,
    loading: Boolean,
    error: ErrorState,
    onClickMoreImages: () -> Unit,
    onDeleteUser: (UserModel) -> Unit,
    navigateToDetail: (String) -> Unit,
) {
    val groupedUsers = users.groupBy { it.name[0] }.toSortedMap()
    val snackBarHostState = remember { SnackbarHostState() }
    val defaultError = stringResource(R.string.default_error)

    LaunchedEffect(error) {
        when (error) {
            is ErrorState.NetworkError -> {
                launch {
                    snackBarHostState.showSnackbar(
                        message = error.message,
                        duration = SnackbarDuration.Short
                    )
                }
            }

            ErrorState.NoError -> {}

            ErrorState.Unknown -> {
                launch {
                    snackBarHostState.showSnackbar(
                        message = defaultError,
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) })
    { contentPadding ->
        Box(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(16.dp)
                    .testTag(TAG_USER_LIST),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                groupedUsers.forEach { (initial, users) ->
                    stickyHeader {
                        Text(text = initial.toString())
                    }

                    items(users) { user ->
                        UserItem(
                            user = user,
                            onDeleteUser = onDeleteUser,
                            navigateToDetail = navigateToDetail
                        )
                    }
                }
            }
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .testTag(TAG_USER_LIST_CIRCULAR_PROGRESS_INDICATOR)
                )
            }

            if (users.isNotEmpty()) {
                ElevatedButton(
                    onClick = onClickMoreImages,
                    enabled = !loading,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .testTag(TAG_MORE_IMAGES_BUTTON)
                ) {
                    Text(text = stringResource(id = R.string.more_images))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserItemPreview() {
    RandomUsersTheme {
        UserItem(
            user = UserModel(
                gender = Gender.MALE,
                id = "1",
                name = "Chris Brown",
                email = "chrisbrown@gmail.com",
                thumbnail = "",
                picture = "",
                phone = "644992298",
                address = "Oxford Street 33",
                location = "Oxford Long River United Kingdom",
                registeredDate = "20-04-2009",
            ),
            onDeleteUser = {},
            navigateToDetail = {}
        )
    }
}

@Composable
fun UserItem(
    user: UserModel,
    onDeleteUser: (UserModel) -> Unit,
    navigateToDetail: (String) -> Unit,
) {
    Card(modifier = Modifier
        .testTag(TAG_USER_LIST_ITEM)
        .clickable { navigateToDetail(user.id) }) {
        Box {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AsyncImage(
                    model = user.picture,
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape),
                    placeholder = painterResource(id = R.drawable.baseline_account_circle_24),
                    error = painterResource(id = R.drawable.baseline_account_circle_24),
                )
                Text(text = user.name)
                Text(text = user.email)
                Text(text = user.phone)
            }
            IconButton(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .testTag(TAG_USER_DELETE_BUTTON),
                onClick = { onDeleteUser(user) }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            }
        }
    }
}

const val TAG_USER_LIST_CIRCULAR_PROGRESS_INDICATOR = "userListCircularProgressIndicator"
const val TAG_USER_LIST = "userList"
const val TAG_USER_LIST_ITEM = "userListItem"
const val TAG_MORE_IMAGES_BUTTON = "moreImages"
const val TAG_USER_DELETE_BUTTON = "userDelete"