package com.fadhlalhafizh.githubuserapplication.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavGitUsersDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addGithubUserToFavorite(favGitUsers: FavGitUsers)

    @Query("SELECT * FROM fav_git_users")
    fun getFavoriteGithubUsers(): LiveData<List<FavGitUsers>>

    @Query("SELECT count(*) FROM fav_git_users WHERE fav_git_users.id = :id")
    fun checkGithubUser(id: Int): Int

    @Query("DELETE FROM fav_git_users WHERE fav_git_users.id = :id")
    fun removeGithubUserFromFavorite(id: Int): Int
}