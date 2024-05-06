package com.hectoruiz.domain.usecases

import com.hectoruiz.domain.models.UserModel
import com.hectoruiz.domain.repositories.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject


class GetUserUseCase @Inject constructor(private val userRepository: UserRepository) {

    fun getUser(email: String): Flow<Result<UserModel>> {
        return userRepository.getUser(email).map { result ->
            result.fold(
                onSuccess = {
                    try {
                        val user = it.copy(registeredDate = it.registeredDate.transformDate())
                        Result.success(user)
                    } catch (e: Exception) {
                        Result.failure(Throwable(PARSING_ERROR))
                    }
                },
                onFailure = {
                    Result.failure(it)
                }
            )
        }
    }

    companion object {
        const val PARSING_ERROR = "Unexpected error"
    }
}

fun String.transformDate(): String {
    val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    val formatter = SimpleDateFormat("dd.MM.yyyy - HH:mm", Locale.getDefault())
    return formatter.format(parser.parse(this))
}
