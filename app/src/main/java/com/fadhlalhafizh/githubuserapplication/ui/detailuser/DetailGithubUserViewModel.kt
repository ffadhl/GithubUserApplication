package com.fadhlalhafizh.githubuserapplication.ui.detailuser

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fadhlalhafizh.githubuserapplication.data.API.response.DetailGithubUserResponse
import com.fadhlalhafizh.githubuserapplication.data.API.retrofit.ApiConfig
import com.fadhlalhafizh.githubuserapplication.data.local.FavGitUsers
import com.fadhlalhafizh.githubuserapplication.data.local.FavGitUsersDao
import com.fadhlalhafizh.githubuserapplication.data.local.FavGitUsersDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailGithubUserViewModel(application: Application) : AndroidViewModel(application) {

    private val githubUserDao: FavGitUsersDao?
    private val githubUserDB: FavGitUsersDatabase?

    init {
        githubUserDB = FavGitUsersDatabase.getGithubUserDatabase(application)
        githubUserDao = githubUserDB.favGitUsersDao()
    }

    fun addToFavoriteGithubUser(id: Int, username: String, avatarUrl: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val favGitUser = FavGitUsers(id = id, login = username, avatarUrl = avatarUrl
            )
            githubUserDao?.addGithubUserToFavorite(favGitUser)
        }
    }

    fun setGithubUserDetail(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailGithubUser(username)
        client.enqueue(object : Callback<DetailGithubUserResponse> {
            override fun onResponse(
                call: Call<DetailGithubUserResponse>,
                response: Response<DetailGithubUserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _detailgithubuser.value = response.body()
                } else {
                    Log.e(TAG, "Failure status Message: ${response.message()}")
                }
                CoroutineScope(Dispatchers.IO).launch {
                    val id = response.body()?.id ?: 0
                    val isGithubUserFavorite = githubUserDao?.checkGithubUser(id) ?: 0
                    withContext(Dispatchers.Main) {
                        _isGithubUserFavorite.value = isGithubUserFavorite > 0
                    }
                }
            }

            override fun onFailure(call: Call<DetailGithubUserResponse>, t: Throwable) {
                Log.e(TAG, "Failure status Message: ${t.message.toString()}")
                _isLoading.value = false
            }
        })
    }

    fun removeGitUserFromFavorites(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            githubUserDao?.removeGithubUserFromFavorite(id)
        }
    }

    private val _detailgithubuser = MutableLiveData<DetailGithubUserResponse>()
    val detailgithubuser: MutableLiveData<DetailGithubUserResponse> = _detailgithubuser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isGithubUserFavorite = MutableLiveData<Boolean>()
    val isGithubUserFavorite: LiveData<Boolean> = _isGithubUserFavorite

    companion object {
        private const val TAG = "DetailGithubUserViewModel"
    }
}