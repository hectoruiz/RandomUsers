package com.hectoruiz.domain.usecases

import com.hectoruiz.domain.models.UserModel
import com.hectoruiz.domain.repositories.UserRepository
import javax.inject.Inject

class DeleteUserUseCase @Inject constructor(private val userRepository: UserRepository) {

    suspend fun deleteUser(user: UserModel) = userRepository.deleteUser(user)
}
