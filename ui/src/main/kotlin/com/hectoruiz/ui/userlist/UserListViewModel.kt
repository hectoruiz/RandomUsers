package com.hectoruiz.ui.userlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hectoruiz.domain.models.UserModel
import com.hectoruiz.domain.usecases.DeleteUserUseCase
import com.hectoruiz.domain.usecases.FetchUsersUseCase
import com.hectoruiz.domain.usecases.GetUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    getUsersUseCase: GetUsersUseCase,
    private val fetchUsersUseCase: FetchUsersUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
) : ViewModel() {

    private val coroutineHandler = CoroutineExceptionHandler { _, throwable ->
        notifyError(ErrorState.NetworkError(throwable.message ?: ""))
        _loading.update { false }
    }
    private val _page = MutableStateFlow(1)
    val page = _page.asStateFlow()
    private val _loading = MutableStateFlow(true)
    val loading = _loading.asStateFlow()
    private val _error = MutableSharedFlow<ErrorState>()
    val error = _error.asSharedFlow()
    val users =
        getUsersUseCase.getUsers().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        fetchUsers()
    }

    fun fetchUsers() {
        viewModelScope.launch(coroutineHandler) {
            _loading.update { true }
            fetchUsersUseCase.getRemoteUsers(_page.value).fold(
                onSuccess = {
                    _page.update { _page.value + 1 }
                },
                onFailure = {
                    notifyError(ErrorState.NetworkError(it.message ?: ""))
                }
            )
            _loading.update { false }
        }
    }

    private fun notifyError(errorState: ErrorState) {
        viewModelScope.launch {
            _error.emit(errorState)
        }
    }

    fun deleteUser(user: UserModel) {
        viewModelScope.launch {
            _loading.update { true }
            val isUserSaved = deleteUserUseCase.deleteUser(user)
            _loading.update { false }
            if (!isUserSaved) notifyError(ErrorState.Unknown)
        }
    }
}
