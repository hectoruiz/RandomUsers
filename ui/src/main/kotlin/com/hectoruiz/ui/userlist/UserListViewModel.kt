package com.hectoruiz.ui.userlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hectoruiz.domain.models.UserModel
import com.hectoruiz.domain.usecases.DeleteUserUseCase
import com.hectoruiz.domain.usecases.FetchUsersUseCase
import com.hectoruiz.domain.usecases.GetUsersUseCase
import com.hectoruiz.domain.commons.Error
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
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

    private val coroutineHandler = CoroutineExceptionHandler { _, _ ->
        notifyError(Error.Network)
        _uiState.update { UserListUiState.NotLoading }
    }

    private val _uiState = MutableStateFlow<UserListUiState>(UserListUiState.NotLoading)
    val uiState = _uiState.asStateFlow()
    private val _error = MutableSharedFlow<Error>()
    val error = _error.asSharedFlow()
    private val keyword = MutableStateFlow("")
    private val allUsers = getUsersUseCase.getUsers()
    val users = combine(allUsers, keyword) { allUsers, searchText ->
        if (searchText.isEmpty()) {
            allUsers
        } else {
            filterUsers(allUsers, searchText)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()).also {
        _uiState.update { UserListUiState.NotLoading }
    }

    private fun filterUsers(allUsers: List<UserModel>, searchText: String): List<UserModel> {
        _uiState.update { UserListUiState.Loading }

        val filteredUsers = allUsers.filter { user ->
            user.name.contains(searchText) || user.email.contains(searchText)
        }
        _uiState.update { UserListUiState.NotLoading }

        return filteredUsers
    }

    init {
        fetchUsers(true)
    }

    private fun fetchUsers(isFirstCall: Boolean) {
        if (isFirstCall) _uiState.update { UserListUiState.Loading }

        viewModelScope.launch(coroutineHandler) {
            fetchUsersUseCase.getRemoteUsers(isFirstCall).fold(
                onSuccess = {
                    // Nothing to do here.
                },
                onFailure = {
                    notifyError(Error.Network)
                }
            )
            _uiState.update { UserListUiState.NotLoading }
        }
    }

    fun searchUsers(texToSearch: String) {
        keyword.update { texToSearch }
    }

    fun getMoreUsers() {
        _uiState.update { UserListUiState.LoadMore }
        fetchUsers(false)
    }

    private fun notifyError(error: Error) {
        viewModelScope.launch(coroutineHandler) {
            _error.emit(error)
        }
    }

    fun deleteUser(user: UserModel) {
        _uiState.update { UserListUiState.Loading }

        viewModelScope.launch(coroutineHandler) {
            val isUserSaved = deleteUserUseCase.deleteUser(user)
            if (!isUserSaved) {
                notifyError(Error.Other)
            }
        }
        _uiState.update { UserListUiState.NotLoading }
    }
}
