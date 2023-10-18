package com.fadhlalhafizh.githubuserapplication.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.fadhlalhafizh.githubuserapplication.data.API.response.GithubUserItems
import com.fadhlalhafizh.githubuserapplication.databinding.ItemlistGithubuserBinding

class GithubUserAdapter (private val itemClickListener: OnItemClickListener) : ListAdapter<GithubUserItems, GithubUserAdapter.MyViewHolder>(DIFF_CALLBACK){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemlistGithubuserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val githubUser = getItem(position)
        holder.bind(githubUser)
    }

    inner class MyViewHolder(private val binding: ItemlistGithubuserBinding) : RecyclerView.ViewHolder(binding.root) {

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
        fun bind(gitUser: GithubUserItems){
            binding.apply {
                Glide.with(itemView).load(gitUser.avatarUrl).transition(DrawableTransitionOptions.withCrossFade()).centerCrop().circleCrop().into(imageviewGithubUser)
                textviewGithubUsername.text = gitUser.login
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(gitUser: GithubUserItems)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<GithubUserItems>() {
            override fun areItemsTheSame(oldItem: GithubUserItems, newItem: GithubUserItems): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: GithubUserItems, newItem: GithubUserItems): Boolean =
                areItemsTheSame(oldItem, newItem)
        }
    }

}