package com.hectoruiz.data.models

import com.hectoruiz.domain.models.Gender
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class UserDataMapperTest {

    @Test
    fun `null api model to list user model`() {
        val userApiModelList: UsersApiModel? = null
        val userModelList = userApiModelList.toModel()

        assertTrue(userModelList.isEmpty())
    }

    @Test
    fun `null list api model to list user model`() {
        val userApiModelList = UsersApiModel(results = null)
        val userModelList = userApiModelList.toModel()

        assertTrue(userModelList.isEmpty())
    }

    @Test
    fun `list null api model to list user model`() {
        val userApiModelList = UsersApiModel(results = listOf(UserApiModel()))
        val userModelList = userApiModelList.toModel()

        assertTrue(userModelList.size == 1)

        assertEquals(Gender.UNSPECIFIED, userModelList[0].gender)
        assertEquals("", userModelList[0].name)
        assertEquals("", userModelList[0].email)
        assertEquals("", userModelList[0].phone)
        assertEquals("", userModelList[0].picture)
        assertEquals("", userModelList[0].thumbnail)
        assertEquals("", userModelList[0].registeredDate)
        assertEquals("", userModelList[0].address)
        assertEquals("", userModelList[0].location)
    }

    @Test
    fun `list api model to list user model`() {
        val userNameApiModel = UserNameApiModel(first = "First Name", last = "Second Name")
        val userPictureApiModel = UserPictureApiModel(large = "large", thumbnail = "thumbnail")
        val userStreetApiModel = UserStreetApiModel(number = 12, name = "Street")
        val userLocationApiModel = UserLocationApiModel(
            userStreetApiModel, city = "city", state = "state",
            country = "country"
        )
        val userRegisteredApiModel = UserRegisteredApiModel(date = "date")

        val userApiModel = UserApiModel(
            gender = "female",
            name = userNameApiModel,
            email = "email",
            picture = userPictureApiModel,
            phone = "phone",
            location = userLocationApiModel,
            registered = userRegisteredApiModel
        )
        val userApiModelList = UsersApiModel(results = listOf(userApiModel))
        val userModelList = userApiModelList.toModel()

        assertTrue(userModelList.size == 1)

        assertEquals(Gender.FEMALE, userModelList[0].gender)
        assertEquals(userApiModel.email, userModelList[0].email)
        assertEquals(userApiModel.phone, userModelList[0].phone)
        assertEquals(userApiModel.picture?.large, userModelList[0].picture)
        assertEquals(userApiModel.picture?.thumbnail, userModelList[0].thumbnail)
        assertEquals(
            userApiModel.location?.street?.name + " - " +
                    userApiModel.location?.street?.number + " " +
                    userApiModel.location?.city,
            userModelList[0].address
        )
        assertEquals(
            userApiModel.location?.state + " " + userApiModel.location?.country,
            userModelList[0].location
        )
        assertTrue(userModelList[0].isActive)
    }
}
