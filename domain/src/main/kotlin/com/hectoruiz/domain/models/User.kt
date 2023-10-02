package com.hectoruiz.domain.models

import kotlinx.serialization.Serializable

enum class Gender {
    MALE,
    FEMALE,
    UNSPECIFIED
}

@Serializable
data class UserModel(
    val gender: Gender,
    val id: String,
    val name: String,
    val email: String,
    val thumbnail: String,
    val picture: String,
    val phone: String,
    val address: String,
    val location: String,
    val registeredDate: String,
    val isActive: Boolean = false,
)
