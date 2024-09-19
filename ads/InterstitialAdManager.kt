package com.cooptech.collagephotoeditor.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import com.cooptech.collagephotoeditor.R
import com.cooptech.collagephotoeditor.utils.AppSettings
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback


/**
---------------------------------------------
This is Interstitial Ad implementation
---------------------------------------------
 */

class InterstitialAdManager(private val context: Context) {

    private val INTERSTITIAL_LOG = "23865783458934445"
    private var interstitialAd: InterstitialAd? = null
    private var isInterstitialAdLoading = false

    companion object {
        var isInterstitialAdShowing = false
        var clickCount = 0
    }

    //load interstitial ad fun
    fun loadInterstitialAd() {

        if (AppSettings.isRemoveAds){
            return
        }

        isInterstitialAdLoading = true

        //request ad
        val adRequest = AdRequest.Builder().build()

        //load Ad
        InterstitialAd.load(
            context,
            context.getString(R.string.interstitial_ad_id),
            adRequest,
            interstitialAdLoadCallback
        )

    }

    private val interstitialAdLoadCallback = object : InterstitialAdLoadCallback() {

        override fun onAdLoaded(ad: InterstitialAd) {
            Log.d(INTERSTITIAL_LOG, "Ad loaded.")
            isInterstitialAdLoading = false
            interstitialAd = ad
        }

        override fun onAdFailedToLoad(error: LoadAdError) {
            Log.d(INTERSTITIAL_LOG, "Ad failed to load.")
            isInterstitialAdLoading = false
            interstitialAd = null
        }

    }

    private fun isInterstitialAdAvailable(): Boolean {
        return interstitialAd != null
    }

    private fun showInterstitialAdIfAvailable(currentActivity: Activity, callback: (AdState) -> Unit) {

        if (isInterstitialAdShowing) {
            Log.d(INTERSTITIAL_LOG, "Ad already showing or Loading.")
            callback(AdState.IS_NOT_LOADED)
            return
        }

        if (!isInterstitialAdAvailable()) {
            Log.d(INTERSTITIAL_LOG, "Ad not loaded.")
            loadInterstitialAd()
            callback(AdState.IS_NOT_LOADED)
            return
        }

        //Interstitial ad load callback
        interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {

            override fun onAdShowedFullScreenContent() {
                Log.d(INTERSTITIAL_LOG, "Ad Showing.")
                callback(AdState.IS_SHOWED)
            }

            override fun onAdDismissedFullScreenContent() {
                Log.d(INTERSTITIAL_LOG, "Ad dismissed.")
                interstitialAd = null
                isInterstitialAdShowing = false
                loadInterstitialAd()
                callback(AdState.IS_DISMISSED)
            }

            override fun onAdFailedToShowFullScreenContent(error: AdError) {
                Log.d(INTERSTITIAL_LOG, "Ad failed to load: ${error.message}")
                isInterstitialAdShowing = false
                callback(AdState.IS_FAILED_TO_LOAD)
            }

        }
        isInterstitialAdShowing = true
        interstitialAd?.show(currentActivity)

    }

    fun showInterstitialAd(currentActivity: Activity, callback: (AdState) -> Unit){
        showInterstitialAdIfAvailable(currentActivity, callback)
    }


}