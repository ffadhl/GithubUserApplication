package com.fadhlalhafizh.githubuserapplication.ui.detailuser

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.fadhlalhafizh.githubuserapplication.data.API.response.FollowersFollowingResponseItem
import com.fadhlalhafizh.githubuserapplication.databinding.ItemlistGithubuserBinding

class FollowersFollowingAdapter(private val itemClickListener: OnItemClickListener) :
    ListAdapter<FollowersFollowingResponseItem, FollowersFollowingAdapter.MyViewHolder>(
        DIFF_CALLBACK
    ) {
    interface OnItemClickListener {
        fun onItemClick(gitUser: FollowersFollowingResponseItem)
    }

    inner class MyViewHolder(private val binding: ItemlistGithubuserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val currentPosition = adapterPosition
                if (currentPosition != RecyclerView.NO_POSITION) {
                    val currentGithubUser = getItem(currentPosition)
                    if (currentGithubUser != null) {
                        itemClickListener.onItemClick(currentGithubUser)
                    }
                }
            }
        }

        fun bind(gitUser: FollowersFollowingResponseItem) {
            binding.apply {
                Glide.with(itemView).load(gitUser.avatarUrl)
                    .transition(DrawableTransitionOptions.withCrossFade()).centerCrop().circleCrop()
                    .into(imageviewGithubUser)
                textviewGithubUsername.text = gitUser.login
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FollowersFollowingResponseItem>() {
            override fun areItemsTheSame(
                oldItem: FollowersFollowingResponseItem,
                newItem: FollowersFollowingResponseItem
            ): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(
                oldItem: FollowersFollowingResponseItem,
                newItem: FollowersFollowingResponseItem
            ): Boolean =
                areItemsTheSame(oldItem, newItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemlistGithubuserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val githubUser = getItem(position)
        holder.bind(githubUser)
    }

}