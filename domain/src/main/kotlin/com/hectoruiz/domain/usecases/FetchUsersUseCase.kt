package com.hectoruiz.domain.usecases

import com.hectoruiz.domain.repositories.UserRepository
import javax.inject.Inject

class FetchUsersUseCase @Inject constructor(private val userRepository: UserRepository) {

    suspend fun getRemoteUsers(page: Int): Result<Unit> = userRepository.getRemoteUsers(page)
}