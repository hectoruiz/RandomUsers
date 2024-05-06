package com.hectoruiz.ui.userdetail

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.hectoruiz.domain.models.Gender
import com.hectoruiz.domain.models.UserModel
import com.hectoruiz.ui.R
import com.hectoruiz.ui.theme.RandomUsersTheme

private val userPreview = UserModel(
    gender = Gender.MALE,
    name = "Chris Brown",
    email = "chrisbrown@gmail.com",
    thumbnail = "",
    picture = "",
    phone = "644992298",
    address = "Oxford Street 33",
    location = "Oxford Long River United Kingdom Long Long loong",
    registeredDate = "20-04-2009",
)

private val userDetailUiStatePreview = UserDetailUiState(user = userPreview)

@Composable
fun UserDetailScreen(userDetailUiState: UserDetailUiState, onBack: () -> Unit) {
    val configuration = LocalConfiguration.current
    if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
        PortraitUserDetailScreen(userDetailUiState) { onBack() }
    } else {
        LandscapeUserDetailScreen(userDetailUiState) { onBack() }
    }
}

@Preview(showBackground = true, apiLevel = 33)
@Composable
fun PortraitUserDetailScreenPreview() {
    RandomUsersTheme {
        PortraitUserDetailScreen(userDetailUiState = userDetailUiStatePreview) {}
    }
}

@Composable
fun PortraitUserDetailScreen(userDetailUiState: UserDetailUiState, onBack: () -> Unit) {
    Scaffold(topBar = {
        Box {
            AsyncImage(
                model = userDetailUiState.user.picture,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxHeight(0.5f)
                    .align(Alignment.TopCenter),
                contentScale = ContentScale.Fit,
                placeholder = painterResource(id = R.drawable.baseline_account_circle_24),
                error = painterResource(id = R.drawable.baseline_account_circle_24),
            )
            IconButton(
                onClick = onBack,
                modifier = Modifier.testTag(TAG_USER_DETAIL_BACK)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null
                )
            }
        }
    }) { contentPadding ->
        Box(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize(),
        ) {
            UserDetail(user = userDetailUiState.user)
            if (userDetailUiState.loading) CircularProgressIndicator(
                modifier = Modifier
                    .testTag(TAG_USER_DETAIL_CIRCULAR_PROGRESS_INDICATOR)
                    .align(Alignment.Center)
            )
        }
    }
}

@Preview(showBackground = true, apiLevel = 33, device = Devices.AUTOMOTIVE_1024p, widthDp = 640)
@Composable
fun LandscapeUserDetailScreenPreview() {
    RandomUsersTheme {
        LandscapeUserDetailScreen(userDetailUiState = userDetailUiStatePreview) {}
    }
}

@Composable
fun LandscapeUserDetailScreen(userDetailUiState: UserDetailUiState, onBack: () -> Unit) {
    Scaffold { contentPadding ->
        Row(modifier = Modifier.padding(contentPadding)) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.5F)
            ) {
                AsyncImage(
                    model = userDetailUiState.user.picture,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.CenterStart),
                    contentScale = ContentScale.FillBounds,
                    placeholder = painterResource(id = R.drawable.baseline_account_circle_24),
                    error = painterResource(id = R.drawable.baseline_account_circle_24),
                )
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.testTag(TAG_USER_DETAIL_BACK)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null
                    )
                }
            }
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                UserDetail(user = userDetailUiState.user)
                if (userDetailUiState.loading) CircularProgressIndicator(
                    modifier = Modifier
                        .testTag(TAG_USER_DETAIL_CIRCULAR_PROGRESS_INDICATOR)
                        .align(Alignment.Center)
                )
            }
        }
    }
}

@Preview(showBackground = true, apiLevel = 33)
@Composable
fun UserDetailPreview() {
    UserDetail(user = userPreview)
}

@Composable
fun UserDetail(user: UserModel) {
    val painterGender = when (user.gender) {
        Gender.MALE -> painterResource(id = R.drawable.male_gender_icon)
        Gender.FEMALE -> painterResource(id = R.drawable.female_gender_icon)
        Gender.UNSPECIFIED -> painterResource(id = R.drawable.undefined_gender_icon)
    }

    Column(
        modifier = Modifier
            .testTag(TAG_USER_DETAIL)
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.Start,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            Icon(
                painter = painterGender,
                contentDescription = null,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Text(text = user.name)
        }
        HorizontalDivider(modifier = Modifier.height(8.dp), thickness = 0.5.dp)

        UserInfo(
            icon = Icons.Filled.Email,
            userInfoComposable = { Text(text = user.email) })
        HorizontalDivider(modifier = Modifier.height(8.dp), thickness = 0.5.dp)

        UserInfo(
            icon = Icons.Filled.Place,
            userInfoComposable = {
                Column {
                    Text(
                        text = user.address,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 2
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = user.location,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 2
                    )
                }
            })
        HorizontalDivider(modifier = Modifier.height(8.dp), thickness = 0.5.dp)

        UserInfo(icon = Icons.Filled.DateRange) {
            Text(text = user.registeredDate)
        }
    }
}


@Preview(showBackground = true, apiLevel = 33)
@Composable
fun UserInfoPreview() {
    RandomUsersTheme {
        UserInfo(icon = Icons.Filled.Face, userInfoComposable = { Text(text = "Chris Brown") })
    }
}

@Composable
fun UserInfo(
    icon: ImageVector, userInfoComposable: @Composable () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
        userInfoComposable()
    }
}

const val TAG_USER_DETAIL = "userInfoDetail"
const val TAG_USER_DETAIL_CIRCULAR_PROGRESS_INDICATOR = "userDetailCircularProgressIndicator"
const val TAG_USER_DETAIL_BACK = "userDetailBack"
