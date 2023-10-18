package com.fadhlalhafizh.githubuserapplication.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fadhlalhafizh.githubuserapplication.data.API.response.GithubUserItems
import com.fadhlalhafizh.githubuserapplication.data.API.response.GithubUserResponse
import com.fadhlalhafizh.githubuserapplication.data.API.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainGithubViewModel : ViewModel(){

    companion object{
        private const val TAG = "MainGithubViewModel"
    }

    fun searchGithubUser(username : String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getSearchGithubUsers(username)
        client.enqueue(object : Callback<GithubUserResponse> {
            override fun onResponse(
                call: Call<GithubUserResponse>,
                response: Response<GithubUserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listGithubUser.value = responseBody.items!!
                    }
                } else {
                    Log.e(TAG, "Failure status Message: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GithubUserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "Failure status Message: ${t.message.toString()}")
            }
        })
    }

    private val _listGithubUser = MutableLiveData<List<GithubUserItems>>()
    val listgithubuser: LiveData<List<GithubUserItems>> = _listGithubUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


}