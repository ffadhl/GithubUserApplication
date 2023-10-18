package com.fadhlalhafizh.githubuserapplication.ui.detailuser

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fadhlalhafizh.githubuserapplication.data.API.response.FollowersFollowingResponseItem
import com.fadhlalhafizh.githubuserapplication.data.API.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowersFollowingViewModel : ViewModel() {

    companion object{
        private const val TAG = "FollowersFollowingViewModel"
    }

    fun setGithubUserFollowers(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getGithubUserFollowers(username)
        client.enqueue(object : Callback<List<FollowersFollowingResponseItem>> {
            override fun onResponse(
                call: Call<List<FollowersFollowingResponseItem>>,
                response: Response<List<FollowersFollowingResponseItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _githubuserfollowers.value = response.body() ?: emptyList()
                } else {
                    Log.e(TAG, "Failure status Message: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<FollowersFollowingResponseItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "Failure status Message: ${t.message.toString()}")
            }
        })
    }

    fun setGithubUserFollowing(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getGithubUserFollowing(username)
        client.enqueue(object : Callback<List<FollowersFollowingResponseItem>> {
            override fun onResponse(
                call: Call<List<FollowersFollowingResponseItem>>,
                response: Response<List<FollowersFollowingResponseItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _githubuserfollowing.value = response.body() ?: emptyList()
                } else {
                    Log.e(TAG, "Failure status Message: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<FollowersFollowingResponseItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "Failure status Message: ${t.message.toString()}")
            }
        })
    }

    private val _githubuserfollowers = MutableLiveData<List<FollowersFollowingResponseItem>>()
    val githubUserFollowers: LiveData<List<FollowersFollowingResponseItem>> = _githubuserfollowers

    private val _githubuserfollowing = MutableLiveData<List<FollowersFollowingResponseItem>>()
    val githubUserFollowing: LiveData<List<FollowersFollowingResponseItem>> = _githubuserfollowing

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

}