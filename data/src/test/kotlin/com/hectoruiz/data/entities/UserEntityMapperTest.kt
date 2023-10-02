package com.hectoruiz.data.entities

import com.hectoruiz.domain.models.Gender
import com.hectoruiz.domain.models.UserModel
import org.junit.Assert.assertEquals
import org.junit.Test

class UserEntityMapperTest {

    private val userEntity = UserEntity(
        id = "1234",
        gender = "male",
        name = "user name",
        email = "user email",
        thumbnail = "user thumbnail",
        picture = "user picture",
        phone = "user phone",
        address = "user address",
        location = "user location",
        registeredDate = "user date",
        isActive = false,
    )

    private val userModel = UserModel(
        id = "5678",
        gender = Gender.UNSPECIFIED,
        name = "user name",
        email = "user email",
        thumbnail = "user thumbnail",
        picture = "user picture",
        phone = "user phone",
        address = "user address",
        location = "user location",
        registeredDate = "user date",
        isActive = false,
    )

    @Test
    fun `list entities to list user model`() {
        val userEntityList = listOf(userEntity, userEntity)
        val userModelList = userEntityList.toModel()

        assertEquals(userEntityList.size, userModelList.size)
        for (i in userEntityList.indices) {
            assertEquals(userEntityList[i].id, userModelList[i].id)
            assertEquals(Gender.MALE, userModelList[i].gender)
            assertEquals(userEntityList[i].name, userModelList[i].name)
            assertEquals(userEntityList[i].email, userModelList[i].email)
            assertEquals(userEntityList[i].thumbnail, userModelList[i].thumbnail)
            assertEquals(userEntityList[i].picture, userModelList[i].picture)
            assertEquals(userEntityList[i].phone, userModelList[i].phone)
            assertEquals(userEntityList[i].address, userModelList[i].address)
            assertEquals(userEntityList[i].location, userModelList[i].location)
            assertEquals(userEntityList[i].registeredDate, userModelList[i].registeredDate)
            assertEquals(userEntityList[i].isActive, userModelList[i].isActive)
        }
    }

    @Test
    fun `entity to user model`() {
        val userModel = userEntity.toModel()

        assertEquals(userEntity.id, userModel.id)
        assertEquals(Gender.MALE, userModel.gender)
        assertEquals(userEntity.name, userModel.name)
        assertEquals(userEntity.email, userModel.email)
        assertEquals(userEntity.thumbnail, userModel.thumbnail)
        assertEquals(userEntity.picture, userModel.picture)
        assertEquals(userEntity.phone, userModel.phone)
        assertEquals(userEntity.address, userModel.address)
        assertEquals(userEntity.location, userModel.location)
        assertEquals(userEntity.registeredDate, userModel.registeredDate)
        assertEquals(userEntity.isActive, userModel.isActive)
    }

    @Test
    fun `list user model to list entities`() {
        val userModelList = listOf(userModel, userModel)
        val userEntityList = userModelList.toEntity()

        assertEquals(userModelList.size, userEntityList.size)
        for (i in userModelList.indices) {
            assertEquals(userModelList[i].id, userEntityList[i].id)
            assertEquals("", userEntityList[i].gender)
            assertEquals(userModelList[i].name, userEntityList[i].name)
            assertEquals(userModelList[i].email, userEntityList[i].email)
            assertEquals(userModelList[i].thumbnail, userEntityList[i].thumbnail)
            assertEquals(userModelList[i].picture, userEntityList[i].picture)
            assertEquals(userModelList[i].phone, userEntityList[i].phone)
            assertEquals(userModelList[i].address, userEntityList[i].address)
            assertEquals(userModelList[i].location, userEntityList[i].location)
            assertEquals(userModelList[i].registeredDate, userEntityList[i].registeredDate)
            assertEquals(userModelList[i].isActive, userEntityList[i].isActive)
        }
    }

    @Test
    fun `user model to entity`() {
        val entity = userModel.toEntity()

        assertEquals(userModel.id, entity.id)
        assertEquals("", entity.gender)
        assertEquals(userModel.name, entity.name)
        assertEquals(userModel.email, entity.email)
        assertEquals(userModel.thumbnail, entity.thumbnail)
        assertEquals(userModel.picture, entity.picture)
        assertEquals(userModel.phone, entity.phone)
        assertEquals(userModel.address, entity.address)
        assertEquals(userModel.location, entity.location)
        assertEquals(userModel.registeredDate, entity.registeredDate)
        assertEquals(userModel.isActive, entity.isActive)
    }
}
