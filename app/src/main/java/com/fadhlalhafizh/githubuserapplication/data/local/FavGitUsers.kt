package com.fadhlalhafizh.githubuserapplication.data.local

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Entity(tableName = "fav_git_users")
@Parcelize
data class FavGitUsers(
    @ColumnInfo (name = "Login") val login: String,
    @PrimaryKey val id: Int,
    @ColumnInfo (name = "avatarUrl") val avatarUrl: String,
) : Serializable, Parcelable
