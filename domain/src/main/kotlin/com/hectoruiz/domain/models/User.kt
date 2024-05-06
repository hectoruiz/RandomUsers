package com.hectoruiz.domain.models

enum class Gender {
    MALE,
    FEMALE,
    UNSPECIFIED
}

data class UserModel(
    val gender: Gender,
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
