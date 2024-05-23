package com.cooptech.pdfeditor.application

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.cooptech.pdfeditor.R
import com.cooptech.pdfeditor.ads.AppOpenAdManager
import com.cooptech.pdfeditor.mvm.viewmodels.MainViewModel
import com.google.android.gms.ads.MobileAds
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class ApplicationClass: Application() {

    @Inject
    lateinit var appOpenAdManager: AppOpenAdManager
    @Inject
    lateinit var remoteConfig: FirebaseRemoteConfig

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        fetchNadActivate()
        addRemoteConfigListener(remoteConfig)

    }

    private fun fetchNadActivate() {
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener {task->
                if (task.isSuccessful) {
                    val updated = task.result
                    Log.e("239575893242134", "Config params updated: $updated")

                    MobileAds.initialize(this){}
                    appOpenAdManager.loadAd()

                } else {
                    Log.e("239575893242134", "Config params not updated")
                }
            }
    }

    private fun addRemoteConfigListener(remoteConfig: FirebaseRemoteConfig) {
        remoteConfig.addOnConfigUpdateListener(object : ConfigUpdateListener {
            override fun onUpdate(configUpdate: ConfigUpdate) {
                Log.e("239575893242134", "Updated keys: " + configUpdate.updatedKeys)

                if (configUpdate.updatedKeys.contains("color") || configUpdate.updatedKeys.contains("app_open_ad_id")) {
                    remoteConfig.activate().addOnCompleteListener {
                    }
                }
            }

            override fun onError(error: FirebaseRemoteConfigException) {
                Log.w("239575893242134", "Config update error with code: " + error.code, error)
            }
        })
    }
}