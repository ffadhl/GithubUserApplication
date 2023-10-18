package com.fadhlalhafizh.githubuserapplication.ui.detailuser

import android.content.res.Configuration
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.view.MenuItem
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.fadhlalhafizh.githubuserapplication.R
import com.fadhlalhafizh.githubuserapplication.data.API.response.DetailGithubUserResponse
import com.fadhlalhafizh.githubuserapplication.databinding.ActivityDetailGithubUserBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailGithubUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailGithubUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailGithubUserBinding.inflate(layoutInflater)

        setContentView(binding.root)

        window.statusBarColor =
            ContextCompat.getColor(this, if (isDarkModeEnabled()) R.color.black else R.color.blue)

        val actionBarGithubUserApp = supportActionBar
        if (actionBarGithubUserApp != null) {
            val isDarkMode = isDarkModeEnabled()
            val actionBarColorRes = if (isDarkMode) R.color.black else R.color.lightBlue
            val textColorRes = if (isDarkMode) R.color.white else R.color.darkBlue

            val actionBarColor =
                ContextCompat.getColor(this@DetailGithubUserActivity, actionBarColorRes)
            val textColor = ContextCompat.getColor(this@DetailGithubUserActivity, textColorRes)

            if (isDarkMode) {
                actionBarGithubUserApp.setHomeAsUpIndicator(R.drawable.ic_back_darkmode)
            } else {
                actionBarGithubUserApp.setHomeAsUpIndicator(R.drawable.ic_back)
            }

            actionBarGithubUserApp.setBackgroundDrawable(ColorDrawable(actionBarColor))
            actionBarGithubUserApp.setDisplayHomeAsUpEnabled(true)

            val title = SpannableString("Detail User")
            title.apply {
                setSpan(
                    ForegroundColorSpan(textColor),
                    0,
                    length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                setSpan(AbsoluteSizeSpan(24, true), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            supportActionBar?.title = title
        }

        val detailGithubUserViewModel =
            ViewModelProvider(this)[DetailGithubUserViewModel::class.java]
        detailGithubUserViewModel.isLoading.observe(this) {
            displayProgressLoadingBar(it)
        }

        val username = intent.getStringExtra(EXTRA_USERNAME)

        detailGithubUserViewModel.setGithubUserDetail(username.toString())
        detailGithubUserViewModel.detailgithubuser.observe(this) {
            if (it != null) {
                binding.apply {
                    Glide.with(binding.root).load(it.avatarUrl).transition(
                        DrawableTransitionOptions.withCrossFade()
                    ).centerCrop().circleCrop().into(binding.imageviewProfileGithubUser)
                    textviewFullnameGithubuser.text = it.name
                    tvUsername.text = it.login
                    textviewGithubUserFollowers.text =
                        resources.getString(R.string.Follower, it.followers)
                    textviewGithubUserFollowing.text =
                        resources.getString(R.string.Following, it.following)
                }
            }
        }


        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = username.toString()
        val viewPager: ViewPager2 = findViewById(R.id.viewpager_detailUser)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs_FollowingFollowers)


        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
            val textColorRes = if (isDarkModeEnabled()) R.color.white else R.color.black
            val textColor = ContextCompat.getColor(this@DetailGithubUserActivity, textColorRes)
            tabs.setTabTextColors(textColor, textColor)
        }.attach()

        val backgroundColor = if (isDarkModeEnabled()) {
            ContextCompat.getColor(this@DetailGithubUserActivity, R.color.black)
        } else {
            ContextCompat.getColor(this@DetailGithubUserActivity, R.color.white)
        }
        tabs.setBackgroundColor(backgroundColor)


        fun showFavToggle(isFavorite: Boolean) {
            this.binding.togglefav.isChecked = isFavorite
        }
        detailGithubUserViewModel.isGithubUserFavorite.observe(this) { isGithubUsersFavorite ->
            showFavToggle(isGithubUsersFavorite)

            fun toggleFavorite(detailGithubUserResponse: DetailGithubUserResponse?) {
                if (detailGithubUserResponse != null) {
                    val username = detailGithubUserResponse.login.toString()
                    val id = detailGithubUserResponse.id.toString().toInt()
                    val avatarUrl = detailGithubUserResponse.avatarUrl.toString()
                    if (binding.togglefav.isChecked) {
                        val detailGithubUserViewModel =
                            ViewModelProvider(this)[DetailGithubUserViewModel::class.java]
                        detailGithubUserViewModel.addToFavoriteGithubUser(id, username, avatarUrl)
                    } else {
                        val detailGithubUserViewModel =
                            ViewModelProvider(this)[DetailGithubUserViewModel::class.java]
                        detailGithubUserViewModel.removeGitUserFromFavorites(id)
                    }
                }
            }
            this.binding.togglefav.setOnClickListener {
                if (detailGithubUserViewModel.detailgithubuser.value != null) {
                    toggleFavorite(detailGithubUserViewModel.detailgithubuser.value)
                }
            }
        }
    }


    private fun displayProgressLoadingBar(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun isDarkModeEnabled(): Boolean {
        return resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            true
        } else {
            super.onOptionsItemSelected(item)
        }


    companion object {
        const val EXTRA_USERNAME = "extra_username"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }

}