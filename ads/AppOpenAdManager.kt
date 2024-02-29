package com.tenx.translator.ads

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback
import com.tenx.translator.R
import com.tenx.translator.ads.InterstitialAdManager.Companion.isInterstitialAdShowing
import com.tenx.translator.baseapplication.ApplicationClass
import com.tenx.translator.utils.AppSettings.Companion.MEDIUM_TIME
import com.tenx.translator.utils.AppSettings.Companion.isSplashScreen
import com.tenx.translator.utils.dialogs.LoadingAdDialog.Companion.showAdLoading
import javax.inject.Inject

/**
    ---------------------------------------------
        This is App open Ad implementation
    ---------------------------------------------
 */

class AppOpenAdManager@Inject constructor(private val mBaseApp: ApplicationClass) :
    ActivityLifecycleCallbacks, LifecycleEventObserver {

    init {
        mBaseApp.registerActivityLifecycleCallbacks(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    private val APP_OPEN_LOG = "357893657836"

    //app open ad
    private var appOpenAd: AppOpenAd? = null

    //app open ad load and visibility
    private var isLoadingAd: Boolean = false
    private var isShowingAd: Boolean = false

    //show ad on first application open
    private var isFirstAppOpen = true

    //get current activity
    private var currentActivity: Activity? = null

    //Ad visibility manager
    private var appOpenVisibilityManager: AdVisibilityController? = null

    private fun isAdAvailable(): Boolean {
        return appOpenAd != null
    }

    fun setAppOpenVisibilityManager(appOpenVisibilityController: AdVisibilityController){
        appOpenVisibilityManager = appOpenVisibilityController
    }

    fun loadAd() {
        if (isLoadingAd || isAdAvailable()) {
            return
        }
        isLoadingAd = true

        val requestAd = AdRequest.Builder().build()
        AppOpenAd.load(
            mBaseApp.applicationContext,
            mBaseApp.getString(R.string.app_open_ad_id),
            requestAd,
            object : AppOpenAdLoadCallback() {
                override fun onAdLoaded(ad: AppOpenAd) {
                    Log.d(APP_OPEN_LOG, "Ad Loaded.")
                    appOpenAd = ad
                    isLoadingAd = false
                    if (isFirstAppOpen){
                        Log.d(APP_OPEN_LOG, "first open app.")
                        isFirstAppOpen = false
                        showAdIfAvailable()
                    }
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.d(APP_OPEN_LOG, "Failed to load Ad: error: $error")
                    isLoadingAd = false
                }
            }
        )

    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

    override fun onActivityStarted(activity: Activity) {
        if (!isShowingAd) {
            currentActivity = activity
        }
    }

    override fun onActivityResumed(activity: Activity) {}

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    override fun onActivityDestroyed(activity: Activity) {
        currentActivity = null
    }


    private fun showAdIfAvailable() {

        //return if app open ad is showing
        if (isShowingAd) {
            Log.d(APP_OPEN_LOG, "Ad already showing")
            return
        }

        //return if app open ad is not loaded
        if (!isAdAvailable()) {
            Log.d(APP_OPEN_LOG, "Ad not available")
            loadAd()
            return
        }

        //app open load callback
        appOpenAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdShowedFullScreenContent() {
                Log.d(APP_OPEN_LOG, "Ad showing")
                appOpenVisibilityManager?.closeAds()
            }

            override fun onAdDismissedFullScreenContent() {
                Log.d(APP_OPEN_LOG, "Ad Dismissed")
                appOpenAd = null
                isShowingAd = false
                appOpenVisibilityManager?.restoreAds()
                loadAd()
            }

            override fun onAdFailedToShowFullScreenContent(error: AdError) {
                Log.d(APP_OPEN_LOG, "Ad Failed to Load: ${error.message}")
                appOpenAd = null
                isShowingAd = false
                loadAd()
            }

        }

        isShowingAd = true
        currentActivity?.let {activity->
            showAdLoading(activity, MEDIUM_TIME){
                appOpenAd?.show(activity)
            }
        }


    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_START) {
            manageAdDisplay()
        }
    }

    private fun manageAdDisplay() {
        if (!isSplashScreen && !isInterstitialAdShowing && !isShowingAd) {
                showAdIfAvailable()
        }
    }


}