package com.fadhlalhafizh.githubuserapplication.ui.settingtheme

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class SettingThemeViewModel (private val pref: SettingPreferences) : ViewModel() {
    fun getThemeSettingsGithubUserApp(): LiveData<Boolean> {
        return pref.getThemeSettingGithubUserApp().asLiveData()
    }

    fun saveThemeSettingGithubUserApp(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSettingGithubUserApp(isDarkModeActive)
        }
    }
}