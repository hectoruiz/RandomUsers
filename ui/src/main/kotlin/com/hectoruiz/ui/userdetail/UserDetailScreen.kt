package com.hectoruiz.ui.userdetail

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.hectoruiz.domain.models.Gender
import com.hectoruiz.domain.models.UserModel
import com.hectoruiz.ui.R
import com.hectoruiz.ui.userlist.theme.RandomUsersTheme

private val userPreview = UserModel(
    gender = Gender.MALE,
    id = "1",
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

@Preview(showBackground = true)
@Composable
fun UserDetailScreenPreview() {
    RandomUsersTheme {
        UserDetailScreen(userDetailUiState = userDetailUiStatePreview)
    }
}

@Composable
fun UserDetailScreen(userDetailUiState: UserDetailUiState) {
    val user = userDetailUiState.user
    val targetColor = when (user.gender) {
        Gender.MALE -> Color.Cyan
        Gender.FEMALE -> Color.Magenta
        Gender.UNSPECIFIED -> Color.Yellow
    }
    val infiniteTransition = rememberInfiniteTransition("card_background_transition")
    val color by infiniteTransition.animateColor(
        initialValue = targetColor.copy(alpha = 0.5f),
        targetValue = targetColor,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "card_background_color"
    )

    Box(
        modifier = Modifier.padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AsyncImage(
                model = user.picture,
                contentDescription = null,
                modifier = Modifier
                    .weight(0.5f)
                    .clip(CircleShape),
                placeholder = painterResource(id = R.drawable.baseline_account_circle_24),
                error = painterResource(id = R.drawable.baseline_account_circle_24),
            )
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        tint = color,
                        modifier = Modifier.size(30.dp),
                        contentDescription = null
                    )
                    Text(text = user.name)
                }
                Text(text = user.email)
                Text(text = user.address, overflow = TextOverflow.Ellipsis, maxLines = 1)
                Text(text = user.location, overflow = TextOverflow.Ellipsis, maxLines = 1)
                Text(text = user.registeredDate)
            }
        }
        if (userDetailUiState.loading) CircularProgressIndicator(
            modifier = Modifier
                .testTag(TAG_USER_DETAIL_CIRCULAR_PROGRESS_INDICATOR)
                .align(Alignment.Center)
        )
    }
}

const val TAG_USER_DETAIL_CIRCULAR_PROGRESS_INDICATOR = "userDetailCircularProgressIndicator"
