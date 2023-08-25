package com.geekster.notewell

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.geekster.notewell.models.UserRequest
import com.geekster.notewell.models.UserResponse
import com.geekster.notewell.repository.UserRepository
import com.geekster.notewell.utlis.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel()  {


    val userResponseLiveData : LiveData<NetworkResult<UserResponse>>
        get() = userRepository.userResponseLiveData
    fun registerUser(userRequest: UserRequest){
        viewModelScope.launch {
            userRepository.registerUser(userRequest)
        }
    }
    fun loginUser(userRequest: UserRequest){
        viewModelScope.launch {
            userRepository.loginUser(userRequest)
        }
    }
}