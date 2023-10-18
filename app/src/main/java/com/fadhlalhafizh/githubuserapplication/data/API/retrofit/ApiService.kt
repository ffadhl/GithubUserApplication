package com.fadhlalhafizh.githubuserapplication.data.API.retrofit

import com.fadhlalhafizh.githubuserapplication.data.API.response.DetailGithubUserResponse
import com.fadhlalhafizh.githubuserapplication.data.API.response.FollowersFollowingResponseItem
import com.fadhlalhafizh.githubuserapplication.data.API.response.GithubUserResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    fun getSearchGithubUsers(
        @Query("q") q: String
    ): Call<GithubUserResponse>

    @GET("users/{username}")
    fun getDetailGithubUser(
        @Path("username") username: String
    ): Call<DetailGithubUserResponse>

    @GET("users/{username}/followers")
    fun getGithubUserFollowers(
        @Path("username") username: String
    ): Call<List<FollowersFollowingResponseItem>>

    @GET("users/{username}/following")
    fun getGithubUserFollowing(
        @Path("username") username: String
    ): Call<List<FollowersFollowingResponseItem>>

}