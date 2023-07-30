package com.geekster.notewell.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.geekster.notewell.utlis.NetworkResult
import com.geekster.notewell.api.UserAPI
import com.geekster.notewell.models.UserRequest
import com.geekster.notewell.models.UserResponse
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(private val userAPI: UserAPI) {

    private val _userResponseLiveData = MutableLiveData<NetworkResult<UserResponse>>()
    val userResponseLiveData: LiveData<NetworkResult<UserResponse>>
        get() = _userResponseLiveData

    suspend fun registerUser(userRequest: UserRequest) {
        _userResponseLiveData.postValue(NetworkResult.Loading())
        _userResponseLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.signup(userRequest)
        responseHandling(response)
    }

    suspend fun loginUser(userRequest: UserRequest) {
        val response = userAPI.signin(userRequest)
        responseHandling(response)
    }

    private fun responseHandling(response: Response<UserResponse>) {
        if (response.isSuccessful && response.body() != null) {
            _userResponseLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _userResponseLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _userResponseLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
    }

}