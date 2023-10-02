package com.hectoruiz.ui.userdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hectoruiz.domain.usecases.GetUserUseCase
import com.hectoruiz.ui.userlist.ErrorState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getUserUseCase: GetUserUseCase,
) : ViewModel() {

    private val userId: String = savedStateHandle["userId"] ?: ""
    private val _userDetailUiState = MutableStateFlow(UserDetailUiState())
    val userDetailUiState: StateFlow<UserDetailUiState> = _userDetailUiState.asStateFlow()

    init {
        viewModelScope.launch {
            getUserUseCase.getUser(userId).collect { result ->
                result.fold(
                    onSuccess = {
                        _userDetailUiState.update { state ->
                            state.copy(loading = false, user = it)
                        }
                    },
                    onFailure = {
                        _userDetailUiState.update { state ->
                            state.copy(loading = false, error = ErrorState.Unknown)
                        }
                    }
                )
            }
        }
    }
}
