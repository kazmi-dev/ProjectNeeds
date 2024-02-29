package com.tenx.translator.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.tenx.translator.R
import com.tenx.translator.utils.AppSettings.Companion.SMALL_TIME
import com.tenx.translator.utils.dialogs.LoadingAdDialog
import com.tenx.translator.utils.dialogs.LoadingAdDialog.Companion.showAdLoading


/**
---------------------------------------------
This is Interstitial Ad implementation
---------------------------------------------
 */

class InterstitialAdManager(private val context: Context) {

    private val INTERSTITIAL_LOG = "23865783458934445"
    private var interstitialAd: InterstitialAd? = null

    companion object {
        var isInterstitialAdShowing = false
        private var clickCount = 0
    }

    //load interstitial ad fun
    fun loadInterstitialAd() {

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
            interstitialAd = ad
        }

        override fun onAdFailedToLoad(error: LoadAdError) {
            Log.d(INTERSTITIAL_LOG, "Ad loaded.")
            interstitialAd = null
        }

    }

    private fun isInterstitialAdAvailable(): Boolean {
        return interstitialAd != null
    }

    private fun showInterstitialAdIfAvailable(currentActivity: Activity) {

        if (isInterstitialAdShowing) {
            Log.d(INTERSTITIAL_LOG, "Ad already showing.")
            return
        }

        if (!isInterstitialAdAvailable()) {
            Log.d(INTERSTITIAL_LOG, "Ad not loaded.")
            loadInterstitialAd()
            return
        }

        //Interstitial ad load callback
        interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {

            override fun onAdShowedFullScreenContent() {
                Log.d(INTERSTITIAL_LOG, "Ad Showing.")
            }

            override fun onAdDismissedFullScreenContent() {
                Log.d(INTERSTITIAL_LOG, "Ad dismissed.")
                loadInterstitialAd()
                interstitialAd = null
                isInterstitialAdShowing = false
            }

            override fun onAdFailedToShowFullScreenContent(error: AdError) {
                Log.d(INTERSTITIAL_LOG, "Ad failed to load: ${error.message}")
                isInterstitialAdShowing = false
            }

        }
        isInterstitialAdShowing = true
        showAdLoading(currentActivity, SMALL_TIME){
            interstitialAd?.show(currentActivity)
        }

    }

    fun showInterstitialAd(currentActivity: Activity){
        if (clickCount == 2){
            clickCount = 0
            Log.d(INTERSTITIAL_LOG, "click count: $clickCount")
            showInterstitialAdIfAvailable(currentActivity)
        }else{
            clickCount++
            Log.d(INTERSTITIAL_LOG, "click count: $clickCount")
        }
    }




}