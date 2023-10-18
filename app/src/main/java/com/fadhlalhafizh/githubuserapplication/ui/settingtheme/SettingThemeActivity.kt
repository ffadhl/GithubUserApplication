package com.fadhlalhafizh.githubuserapplication.ui.settingtheme

import android.content.res.Configuration
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.view.MenuItem
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.fadhlalhafizh.githubuserapplication.R
import com.fadhlalhafizh.githubuserapplication.databinding.ActivitySettingThemeBinding

class SettingThemeActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingThemeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingThemeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor =
            ContextCompat.getColor(this, if (isDarkModeEnabled()) R.color.black else R.color.blue)

        val actionBarGithubUserApp = supportActionBar
        if (actionBarGithubUserApp != null) {
            val isDarkMode = isDarkModeEnabled()
            val actionBarColorRes = if (isDarkMode) R.color.black else R.color.lightBlue
            val textColorRes = if (isDarkMode) R.color.white else R.color.darkBlue

            val actionBarColor = ContextCompat.getColor(this@SettingThemeActivity, actionBarColorRes)
            val textColor = ContextCompat.getColor(this@SettingThemeActivity, textColorRes)

            if (isDarkMode) {
                actionBarGithubUserApp.setHomeAsUpIndicator(R.drawable.ic_back_darkmode)
            } else {
                actionBarGithubUserApp.setHomeAsUpIndicator(R.drawable.ic_back)
            }

            actionBarGithubUserApp.setBackgroundDrawable(ColorDrawable(actionBarColor))
            actionBarGithubUserApp.setDisplayHomeAsUpEnabled(true)

            val title = SpannableString("Setting Theme")
            title.apply {
                setSpan(ForegroundColorSpan(textColor), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                setSpan(AbsoluteSizeSpan(24, true), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            supportActionBar?.title = title
        }

        val switchTheme = binding.switchTheme
        val pref = SettingPreferences.getInstanceGithubUserApp(application.dataStore)
        val mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(pref)
        )[SettingThemeViewModel::class.java]
        mainViewModel.getThemeSettingsGithubUserApp().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchTheme.isChecked = false
            }
        }

        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            mainViewModel.saveThemeSettingGithubUserApp(isChecked)
        }
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
}