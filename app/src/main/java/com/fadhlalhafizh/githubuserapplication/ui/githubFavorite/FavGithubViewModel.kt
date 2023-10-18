package com.fadhlalhafizh.githubuserapplication.ui.githubFavorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fadhlalhafizh.githubuserapplication.data.API.response.GithubUserItems
import com.fadhlalhafizh.githubuserapplication.data.local.FavGitUsers
import com.fadhlalhafizh.githubuserapplication.data.local.FavGitUsersDao
import com.fadhlalhafizh.githubuserapplication.data.local.FavGitUsersDatabase

class FavGithubViewModel(application: Application) : AndroidViewModel(application) {

    private val githubUserDao: FavGitUsersDao?
    private val githubUserDB: FavGitUsersDatabase?

    init {
        githubUserDB = FavGitUsersDatabase.getGithubUserDatabase(application)
        githubUserDao = githubUserDB?.favGitUsersDao()
    }

    fun getFavGithubUsers(): LiveData<List<FavGitUsers>>? {
        return githubUserDao?.getFavoriteGithubUsers()
    }

    private fun FavGitUsers.toItemsItem() =
        GithubUserItems(id = this.id, login = this.login, avatarUrl = this.avatarUrl)


    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


}