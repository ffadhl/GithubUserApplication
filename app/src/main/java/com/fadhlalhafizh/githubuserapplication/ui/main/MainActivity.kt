package com.fadhlalhafizh.githubuserapplication.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.fadhlalhafizh.githubuserapplication.R
import com.fadhlalhafizh.githubuserapplication.data.API.response.GithubUserItems
import com.fadhlalhafizh.githubuserapplication.databinding.ActivityMainBinding
import com.fadhlalhafizh.githubuserapplication.ui.about.CreatorAboutActivity
import com.fadhlalhafizh.githubuserapplication.ui.detailuser.DetailGithubUserActivity
import com.fadhlalhafizh.githubuserapplication.ui.githubFavorite.FavGithubUsersActivity
import com.fadhlalhafizh.githubuserapplication.ui.settingtheme.SettingPreferences
import com.fadhlalhafizh.githubuserapplication.ui.settingtheme.SettingThemeActivity
import com.fadhlalhafizh.githubuserapplication.ui.settingtheme.SettingThemeViewModel
import com.fadhlalhafizh.githubuserapplication.ui.settingtheme.ViewModelFactory
import com.fadhlalhafizh.githubuserapplication.ui.settingtheme.dataStore

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var animationView: LottieAnimationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val pref = SettingPreferences.getInstanceGithubUserApp(application.dataStore)
        val themeViewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(
            SettingThemeViewModel::class.java
        )
        themeViewModel.getThemeSettingsGithubUserApp().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
        animationView = binding.animationView

        supportActionBar?.apply {
            val isDarkMode = isDarkModeEnabled()

            val actionBarColorRes = if (isDarkMode) {
                R.color.black
            } else {
                R.color.lightBlue
            }

            val textColorRes = if (isDarkMode) R.color.white else R.color.darkBlue

            val actionBarColor = ContextCompat.getColor(this@MainActivity, actionBarColorRes)
            val textColor = ContextCompat.getColor(this@MainActivity, textColorRes)

            setBackgroundDrawable(
                ColorDrawable(actionBarColor)
            )

            val title = SpannableString("Github App")
            title.apply {
                setSpan(ForegroundColorSpan(textColor), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                setSpan(AbsoluteSizeSpan(24, true), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            supportActionBar?.title = title

            setDisplayHomeAsUpEnabled(false)
        }

        window.statusBarColor =
            ContextCompat.getColor(this, if (isDarkModeEnabled()) R.color.black else R.color.blue)


        val mainGithubViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[MainGithubViewModel::class.java]
        mainGithubViewModel.listgithubuser.observe(this) { githubUsers ->
            githubUserList(githubUsers)
        }

        mainGithubViewModel.isLoading.observe(this) {
            displayProgressLoadingBar(it)
        }

        with(binding) {
            userSearchView.setupWithSearchBar(userSearch)
            userSearchView.editText
                .setOnEditorActionListener { _, _, _ ->
                    val username = userSearchView.text.toString()
                    animationView.visibility = View.VISIBLE
                    animationView.playAnimation()
                    mainGithubViewModel.searchGithubUser(username)
                    userSearchView.hide()
                    false
                }
        }

        mainGithubViewModel.isLoading.observe(this) {
            displayProgressLoadingBar(it)

            if (it) {
                animationView.visibility = View.GONE
                animationView.cancelAnimation()
            }
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvGithubUsers.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvGithubUsers.addItemDecoration(itemDecoration)

        val adapter = GithubUserAdapter(object : GithubUserAdapter.OnItemClickListener {
            override fun onItemClick(gitUser: GithubUserItems) {
                val detailIntent = Intent(this@MainActivity, DetailGithubUserActivity::class.java)
                detailIntent.putExtra(DetailGithubUserActivity.EXTRA_USERNAME, gitUser.login)
                startActivity(detailIntent)
            }
        })
        binding.rvGithubUsers.adapter = adapter
    }

    private fun githubUserList(githubUser: List<GithubUserItems>) {
        val adapter = binding.rvGithubUsers.adapter as GithubUserAdapter
        adapter.submitList(githubUser)
        binding.rvGithubUsers.adapter = adapter
        animationView.visibility = View.GONE
        animationView.cancelAnimation()
    }

    override fun onCreateOptionsMenu(menuAppbar: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_appbar, menuAppbar)
        return super.onCreateOptionsMenu(menuAppbar)
    }


    override fun onOptionsItemSelected(itemMenuAppbar: MenuItem): Boolean {
        when (itemMenuAppbar.itemId) {
            R.id.menu_about -> {
                val intent = Intent(this, CreatorAboutActivity::class.java)
                startActivity(intent)
            }
        }
        when (itemMenuAppbar.itemId){
            R.id.menu_themesetting -> {
                val intent = Intent(this,SettingThemeActivity::class.java)
                startActivity(intent)
            }
        }
        when (itemMenuAppbar.itemId){
            R.id.menu_fav -> {
                val intent = Intent(this, FavGithubUsersActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(itemMenuAppbar)
    }

    private fun displayProgressLoadingBar (isLoading : Boolean){
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


    private fun isDarkModeEnabled(): Boolean {
        return resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if (menu != null) {
            val pref = SettingPreferences.getInstanceGithubUserApp(application.dataStore)
            val favicon = menu.findItem(R.id.menu_fav)
            val themeSetting = menu.findItem(R.id.menu_themesetting)
            val about = menu.findItem(R.id.menu_about)
            val themeViewModel = ViewModelProvider(this,
                ViewModelFactory(pref))[SettingThemeViewModel::class.java]
            themeViewModel.getThemeSettingsGithubUserApp().observe(this) { isDarkModeActive: Boolean ->
                if (isDarkModeActive) {
                    favicon.icon = getDrawable(R.drawable.ic_favwhite)
                    themeSetting.icon = getDrawable(R.drawable.icon_setting)
                    about.icon = getDrawable(R.drawable.ic_profile)
                } else {
                    favicon.icon = getDrawable(R.drawable.ic_favdarkblue)
                    themeSetting.icon = getDrawable(R.drawable.icon_setting_darkblue)
                    about.icon = getDrawable(R.drawable.ic_profile_darkblue)
                }
            }


        }
        return super.onPrepareOptionsMenu(menu)
    }
}