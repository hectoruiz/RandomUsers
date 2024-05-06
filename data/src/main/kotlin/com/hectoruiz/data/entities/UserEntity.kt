package com.hectoruiz.data.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.hectoruiz.data.models.FEMALE
import com.hectoruiz.data.models.MALE
import com.hectoruiz.domain.models.Gender
import com.hectoruiz.domain.models.UserModel

@Entity(tableName = USERS, indices = [Index(value = ["email"], unique = true)])
data class UserEntity(
    val gender: String? = null,
    val name: String? = null,
    @PrimaryKey
    val email: String,
    val thumbnail: String? = null,
    val picture: String? = null,
    val phone: String? = null,
    val address: String? = null,
    val location: String? = null,
    val registeredDate: String? = null,
    val isActive: Boolean? = null,
)

fun List<UserEntity>.toModel() = this.map { it.toModel() }

fun UserEntity.toModel() =
    UserModel(
        gender = when (this.gender) {
            MALE -> Gender.MALE
            FEMALE -> Gender.FEMALE
            else -> Gender.UNSPECIFIED
        },
        name = this.name.orEmpty(),
        email = this.email,
        phone = this.phone.orEmpty(),
        picture = this.picture.orEmpty(),
        thumbnail = this.thumbnail.orEmpty(),
        registeredDate = this.registeredDate.orEmpty(),
        address = this.address.orEmpty(),
        location = this.location.orEmpty(),
        isActive = this.isActive ?: true
    )

fun List<UserModel>.toEntity(): List<UserEntity> {
    return if (this.isEmpty()) emptyList()
    else {
        this.map { it.toEntity() }
    }
}

fun UserModel.toEntity() = UserEntity(
    gender = when (this.gender) {
        Gender.MALE -> MALE
        Gender.FEMALE -> FEMALE
        else -> ""
    },
    name = this.name,
    email = this.email,
    thumbnail = this.thumbnail,
    picture = this.picture,
    phone = this.phone,
    address = this.address,
    location = this.location,
    registeredDate = this.registeredDate,
    isActive = this.isActive
)

const val USERS = "users"
