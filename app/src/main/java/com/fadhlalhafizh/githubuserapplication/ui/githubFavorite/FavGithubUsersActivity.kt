package com.fadhlalhafizh.githubuserapplication.ui.githubFavorite

import android.content.Intent
import android.content.res.Configuration
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.fadhlalhafizh.githubuserapplication.R
import com.fadhlalhafizh.githubuserapplication.data.API.response.GithubUserItems
import com.fadhlalhafizh.githubuserapplication.data.local.FavGitUsers
import com.fadhlalhafizh.githubuserapplication.databinding.ActivityFavGithubUsersBinding
import com.fadhlalhafizh.githubuserapplication.ui.detailuser.DetailGithubUserActivity
import com.fadhlalhafizh.githubuserapplication.ui.main.GithubUserAdapter

class FavGithubUsersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavGithubUsersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavGithubUsersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBarGithubUserApp = supportActionBar
        if (actionBarGithubUserApp != null) {
            val isDarkMode = isDarkModeEnabled()
            val actionBarColorRes = if (isDarkMode) R.color.black else R.color.lightBlue
            val textColorRes = if (isDarkMode) R.color.white else R.color.darkBlue

            val actionBarColor = ContextCompat.getColor(this@FavGithubUsersActivity, actionBarColorRes)
            val textColor = ContextCompat.getColor(this@FavGithubUsersActivity, textColorRes)

            if (isDarkMode) {
                actionBarGithubUserApp.setHomeAsUpIndicator(R.drawable.ic_back_darkmode)
            } else {
                actionBarGithubUserApp.setHomeAsUpIndicator(R.drawable.ic_back)
            }

            actionBarGithubUserApp.setBackgroundDrawable(ColorDrawable(actionBarColor))
            actionBarGithubUserApp.setDisplayHomeAsUpEnabled(true)

            val title = SpannableString("Favorite Users")
            title.apply {
                setSpan(ForegroundColorSpan(textColor), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                setSpan(AbsoluteSizeSpan(24, true), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            supportActionBar?.title = title
        }

        window.statusBarColor =
            ContextCompat.getColor(this, if (isDarkModeEnabled()) R.color.black else R.color.blue)

        val favGithubUserViewModel = ViewModelProvider(this).get(FavGithubViewModel::class.java)
        favGithubUserViewModel.isLoading.observe(this) {
            displayProgressLoadingBar(it)
        }

        setupRecyclerView()

        val adapter = GithubUserAdapter(object : GithubUserAdapter.OnItemClickListener {
            override fun onItemClick(gitUser: GithubUserItems) {
                val detailIntent =
                    Intent(this@FavGithubUsersActivity, DetailGithubUserActivity::class.java)
                detailIntent.putExtra(DetailGithubUserActivity.EXTRA_USERNAME, gitUser.login)
                startActivity(detailIntent)
            }
        })

        binding.rvGithubUsers.adapter = adapter

        favGithubUserViewModel.getFavGithubUsers()?.observe(this, Observer<List<FavGitUsers>> { githubUserFav ->
            val mappedList = githubUserFav.map { favoriteUser ->
                GithubUserItems(id = favoriteUser.id, login = favoriteUser.login, avatarUrl = favoriteUser.avatarUrl
                )
            }
            adapter.submitList(mappedList)
        })
    }

    private fun displayProgressLoadingBar(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            true
        } else {
            super.onOptionsItemSelected(item)
        }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        binding.rvGithubUsers.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvGithubUsers.addItemDecoration(itemDecoration)
        binding.rvGithubUsers.adapter
    }

    private fun isDarkModeEnabled(): Boolean {
        return resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    }
}