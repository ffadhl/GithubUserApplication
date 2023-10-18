package com.fadhlalhafizh.githubuserapplication.ui.detailuser

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.fadhlalhafizh.githubuserapplication.data.API.response.FollowersFollowingResponseItem
import com.fadhlalhafizh.githubuserapplication.databinding.FragmentFollowersBinding

class FollowersFollowingFragment : Fragment() {

    private lateinit var binding: FragmentFollowersBinding

    private var position: Int = 0
    private var username: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       val followersFollowingViewModel = ViewModelProvider(this)[FollowersFollowingViewModel::class.java]
        followersFollowingViewModel.isLoading.observe(viewLifecycleOwner) {
            displayLoadingBar(it)
        }

        setupRecyclerView()

        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME) ?: ""
        }

        if (position == 1) {
            followersFollowingViewModel.setGithubUserFollowers(username)
            followersFollowingViewModel.githubUserFollowers.observe(viewLifecycleOwner) { followersList ->
                adapter.submitList(followersList)
            }
        } else {
            followersFollowingViewModel.setGithubUserFollowing(username)
            followersFollowingViewModel.githubUserFollowing.observe(viewLifecycleOwner) { followingList ->
                adapter.submitList(followingList)
            }
        }

        displayLoadingBar(true)
    }

    private val adapter = FollowersFollowingAdapter(object : FollowersFollowingAdapter.OnItemClickListener {
        override fun onItemClick(githubUser: FollowersFollowingResponseItem) {
            val detailIntent = Intent(activity, DetailGithubUserActivity::class.java)
            detailIntent.putExtra(DetailGithubUserActivity.EXTRA_USERNAME, githubUser.login)
            startActivity(detailIntent)
        }
    })

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvGithubUsers.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireContext(), layoutManager.orientation)
        binding.rvGithubUsers.addItemDecoration(itemDecoration)
        binding.rvGithubUsers.adapter = adapter
    }

    private fun displayLoadingBar(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val ARG_POSITION = "position"
        const val ARG_USERNAME = "username"
    }
}

