package com.geekster.notewell.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.geekster.notewell.utlis.NetworkResult
import com.geekster.notewell.api.UserAPI
import com.geekster.notewell.models.UserRequest
import com.geekster.notewell.models.UserResponse
import com.geekster.notewell.utlis.Constants.TAG
import javax.inject.Inject

class UserRepository @Inject constructor (private val userAPI : UserAPI) {

    private val _userResponseLiveData = MutableLiveData<NetworkResult<UserResponse>>()
    val userResponseLiveData : LiveData<NetworkResult<UserResponse>>
        get() = _userResponseLiveData

    suspend fun registerUser(userRequest: UserRequest){
        _userResponseLiveData.postValue(NetworkResult.Loading())
        _userResponseLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.signup(userRequest)
        if(response.isSuccessful && response.body() != null){
            _userResponseLiveData.postValue(NetworkResult.Success(response.body()!!))
        }
        else if(response.errorBody() != null){
            _userResponseLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
        else{
            _userResponseLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
    }

    suspend fun loginUser(userRequest: UserRequest){
        val response = userAPI.signin(userRequest)
        Log.d(TAG, response.body().toString())
    }
}