package com.fadhlalhafizh.githubuserapplication.ui.about

import android.content.res.Configuration
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.fadhlalhafizh.githubuserapplication.R
import com.fadhlalhafizh.githubuserapplication.databinding.ActivityCreatorAboutBinding

class CreatorAboutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreatorAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatorAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBarGithubUserApp = supportActionBar
        if (actionBarGithubUserApp != null) {
            val isDarkMode = isDarkModeEnabled()
            val actionBarColorRes = if (isDarkMode) R.color.black else R.color.lightBlue
            val textColorRes = if (isDarkMode) R.color.white else R.color.darkBlue

            val actionBarColor = ContextCompat.getColor(this@CreatorAboutActivity, actionBarColorRes)
            val textColor = ContextCompat.getColor(this@CreatorAboutActivity, textColorRes)

            if (isDarkMode) {
                actionBarGithubUserApp.setHomeAsUpIndicator(R.drawable.ic_back_darkmode)
            } else {
                actionBarGithubUserApp.setHomeAsUpIndicator(R.drawable.ic_back)
            }

            actionBarGithubUserApp.setBackgroundDrawable(ColorDrawable(actionBarColor))
            actionBarGithubUserApp.setDisplayHomeAsUpEnabled(true)

            val title = SpannableString("About Me")
            title.apply {
                setSpan(ForegroundColorSpan(textColor), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                setSpan(AbsoluteSizeSpan(24, true), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            supportActionBar?.title = title
        }

        window.statusBarColor =
            ContextCompat.getColor(this, if (isDarkModeEnabled()) R.color.black else R.color.blue)

        val pembuatDetailName = binding.fadhlalhafizh
        val pembuatEmail = binding.emailbangkitfadhl

        val namaPembuat = resources.getString(R.string.name)
        val emailPembuat= resources.getString(R.string.bangkitemail)

        pembuatDetailName.text = namaPembuat
        pembuatEmail.text = emailPembuat
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