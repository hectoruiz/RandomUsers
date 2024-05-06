package com.hectoruiz.ui.userlist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.hectoruiz.domain.models.Gender
import com.hectoruiz.domain.models.UserModel
import com.hectoruiz.domain.commons.Error
import com.hectoruiz.ui.R
import com.hectoruiz.ui.theme.RandomUsersTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val usersPreview = listOf(
    UserModel(
        gender = Gender.MALE,
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

@Preview(showBackground = true, apiLevel = 33)
@Composable
fun UserListScreenPreview() {
    RandomUsersTheme {
        UserListScreen(
            users = usersPreview,
            uiState = UserListUiState.Loading,
            error = Error.NoError,
            onUserSearch = {},
            onClickMoreUsers = {},
            onDeleteUser = {},
            navigateToDetail = {},
        )
    }
}

@Composable
fun UserListScreen(
    users: List<UserModel>,
    uiState: UserListUiState,
    error: Error,
    onUserSearch: (String) -> Unit,
    onClickMoreUsers: () -> Unit,
    onDeleteUser: (UserModel) -> Unit,
    navigateToDetail: (String) -> Unit,
) {
    val lazyListState: LazyListState = rememberLazyListState()
    val buttonVisibility = uiState == UserListUiState.NotLoading && !lazyListState.canScrollForward
    val snackBarHostState = remember { SnackbarHostState() }
    val networkError = stringResource(id = R.string.network_error)
    val defaultError = stringResource(id = R.string.default_error)

    LaunchedEffect(error) {
        when (error) {
            Error.Network -> {
                launch {
                    snackBarHostState.showSnackbar(
                        message = networkError,
                        duration = SnackbarDuration.Short
                    )
                }
            }

            Error.Other -> {
                launch {
                    snackBarHostState.showSnackbar(
                        message = defaultError,
                        duration = SnackbarDuration.Short
                    )
                }
            }

            Error.NoError -> {}
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) })
    { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
        ) {
            UserSearcher(onUserSearch)

            Spacer(modifier = Modifier.height(16.dp))

            UserList(
                users,
                uiState,
                lazyListState,
                buttonVisibility,
                onDeleteUser,
                navigateToDetail,
                onClickMoreUsers
            )
        }
    }
}

@Preview(showBackground = true, apiLevel = 33)
@Composable
fun UserListPreview() {
    RandomUsersTheme {
        UserList(
            usersPreview,
            UserListUiState.NotLoading,
            rememberLazyListState(),
            true,
            {},
            {},
            {})
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UserList(
    users: List<UserModel>,
    uiState: UserListUiState,
    lazyListState: LazyListState,
    buttonVisibility: Boolean,
    onDeleteUser: (UserModel) -> Unit,
    navigateToDetail: (String) -> Unit,
    onClickMoreUsers: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .padding(bottom = 32.dp)
                .testTag(TAG_USER_LIST),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(users, key = { user -> user.email }) { user ->
                UserItem(
                    modifier = Modifier.animateItemPlacement(
                        animationSpec = tween(
                            durationMillis = ANIMATION_PLACEMENT_DURATION,
                            easing = FastOutLinearInEasing,
                        )
                    ),
                    user = user,
                    onDeleteUser = onDeleteUser,
                    navigateToDetail = navigateToDetail
                )
            }
        }
        when (uiState) {
            UserListUiState.Loading -> CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .testTag(TAG_LOADING_USERS)
            )

            UserListUiState.LoadMore -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .testTag(TAG_LOAD_MORE_USERS)
                )
            }

            UserListUiState.NotLoading -> {}
        }

        AnimatedVisibility(
            visible = buttonVisibility,
            enter = fadeIn(tween(ANIMATION_VISIBILITY_DURATION)),
            exit = fadeOut(tween(ANIMATION_VISIBILITY_DURATION)),
            modifier = Modifier
                .align(Alignment.BottomCenter)
        ) {
            ElevatedButton(
                onClick = onClickMoreUsers,
                modifier = Modifier.testTag(TAG_MORE_USERS_BUTTON)
            ) {
                Text(text = stringResource(id = R.string.more_users))
            }
        }
    }
}

@Preview(showBackground = true, apiLevel = 33)
@Composable
fun UserSearcherPreview() {
    RandomUsersTheme {
        UserSearcher {}
    }
}

@Composable
fun UserSearcher(onSearch: (String) -> Unit) {
    var inputText by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(inputText) {
        if (inputText.isBlank()) onSearch("")

        delay(TYPING_DELAY_COMPLETION)
        onSearch(inputText)
    }

    TextField(
        value = inputText,
        modifier = Modifier
            .fillMaxWidth()
            .testTag(TAG_USER_SEARCH),
        onValueChange = { inputText = it.lowercase() },
        placeholder = {
            Text(
                text = stringResource(id = R.string.search_default),
                fontSize = 12.sp,
            )
        },
        trailingIcon = {
            if (inputText.isNotEmpty()) {
                IconButton(
                    onClick = { inputText = "" },
                    modifier = Modifier.testTag(TAG_CLEAR_SEARCH)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = null,
                    )
                }
            }
        }
    )
}

@Preview(showBackground = true, apiLevel = 33)
@Composable
fun UserItemPreview() {
    RandomUsersTheme {
        UserItem(
            user = UserModel(
                gender = Gender.MALE,
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
    modifier: Modifier = Modifier,
    user: UserModel,
    onDeleteUser: (UserModel) -> Unit,
    navigateToDetail: (String) -> Unit,
) {
    Card(modifier = modifier
        .testTag(TAG_USER_LIST_ITEM)
        .clickable { navigateToDetail(user.email) }) {
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

const val ANIMATION_VISIBILITY_DURATION = 1000
const val ANIMATION_PLACEMENT_DURATION = 500
const val TYPING_DELAY_COMPLETION = 200L
const val TAG_LOADING_USERS = "loadingUsers"
const val TAG_LOAD_MORE_USERS = "loadMoreUsers"
const val TAG_USER_LIST = "userList"
const val TAG_USER_LIST_ITEM = "userListItem"
const val TAG_MORE_USERS_BUTTON = "moreUsers"
const val TAG_USER_DELETE_BUTTON = "userDelete"
const val TAG_USER_SEARCH = "userSearch"
const val TAG_CLEAR_SEARCH = "clearSearch"
