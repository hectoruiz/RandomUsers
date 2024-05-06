package com.hectoruiz.domain.usecases

import com.hectoruiz.domain.models.UserModel
import com.hectoruiz.domain.repositories.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(private val userRepository: UserRepository) {

    fun getUsers(): Flow<List<UserModel>> =
        userRepository.getUsers().map { userList -> userList.filter { it.isActive } }
}
