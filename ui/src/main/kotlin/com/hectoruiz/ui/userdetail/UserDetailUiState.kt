package com.hectoruiz.ui.userdetail

import com.hectoruiz.domain.models.Gender
import com.hectoruiz.domain.models.UserModel
import com.hectoruiz.ui.userlist.ErrorState

data class UserDetailUiState(
    val loading: Boolean = true,
    val user: UserModel = UserModel(
        gender = Gender.UNSPECIFIED,
        id = "",
        name = "",
        email = "",
        phone = "",
        picture = "",
        thumbnail = "",
        registeredDate = "",
        address = "",
        location = "",
        isActive = false
    ),
    val error: ErrorState = ErrorState.NoError,
)