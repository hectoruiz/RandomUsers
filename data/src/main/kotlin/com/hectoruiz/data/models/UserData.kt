package com.hectoruiz.data.models

import com.hectoruiz.domain.models.Gender
import com.hectoruiz.domain.models.UserModel
import kotlinx.serialization.Serializable

@Serializable
data class UsersApiModel(
    val results: List<UserApiModel?>? = null,
)

@Serializable
data class UserApiModel(
    val gender: String? = null,
    val name: UserNameApiModel? = null,
    val email: String? = null,
    val picture: UserPictureApiModel? = null,
    val phone: String? = null,
    val location: UserLocationApiModel? = null,
    val registered: UserRegisteredApiModel? = null,
)

@Serializable
data class UserNameApiModel(
    val first: String? = null,
    val last: String? = null,
)

@Serializable
data class UserPictureApiModel(
    val large: String? = null,
    val thumbnail: String? = null,
)

@Serializable
data class UserLocationApiModel(
    val street: UserStreetApiModel? = null,
    val city: String? = null,
    val state: String? = null,
    val country: String? = null,
)

@Serializable
data class UserStreetApiModel(
    val number: Int? = null,
    val name: String? = null,
)

@Serializable
data class UserRegisteredApiModel(
    val date: String? = null,
)

fun UsersApiModel?.toModel() = this?.results?.map { it.toModel() } ?: emptyList()

fun UserApiModel?.toModel() =
    UserModel(
        gender = when (this?.gender) {
            MALE -> Gender.MALE
            FEMALE -> Gender.FEMALE
            else -> Gender.UNSPECIFIED
        },
        name = listOfNotNull(this?.name?.first, this?.name?.last).joinToString(" "),
        email = this?.email.orEmpty(),
        phone = this?.phone.orEmpty(),
        picture = this?.picture?.large.orEmpty(),
        thumbnail = this?.picture?.thumbnail.orEmpty(),
        registeredDate = this?.registered?.date.orEmpty(),
        address = listOfNotNull(
            listOfNotNull(
                this?.location?.street?.name,
                this?.location?.street?.number,
            ).joinToString(" - "), this?.location?.city
        ).joinToString(" "),
        location = listOfNotNull(this?.location?.state, this?.location?.country).joinToString(" "),
        isActive = true
    )

const val MALE = "male"
const val FEMALE = "female"
