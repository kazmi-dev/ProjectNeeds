package com.cooptech.pdfreader.ads

import android.app.Activity
import android.content.Context
import com.cooptech.pdfreader.R
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback

class RewardedInterstitialAdManager(private val context: Context) {

    private var rewardedInterstitialAd: RewardedInterstitialAd? = null
    private var rewardedInterstitialAdUnitId: String =
        context.getString(R.string.reward_interstitial_ad)
    private var isAdLoading: Boolean = false
    private var isRewardedInterstitialShowing: Boolean = false

    private fun rewardedInterstitialAdLoadCallback(onLoad: () -> Unit) =
        object : RewardedInterstitialAdLoadCallback() {
            override fun onAdLoaded(ad: RewardedInterstitialAd) {
                rewardedInterstitialAd = ad
                isAdLoading = false
                onLoad.invoke()
            }

            override fun onAdFailedToLoad(error: LoadAdError) {
                isAdLoading = false
                rewardedInterstitialAd = null
            }
        }

    fun setRewardedInterstitialAdUnitId(adUnitId: String) {
        this@RewardedInterstitialAdManager.rewardedInterstitialAdUnitId = adUnitId
    }

    fun loadAndShowRewardedInterstitialAd(
        activity: Activity,
        onRewardedAd: (RewardItem) -> Unit,
        adStates: (AdStates) -> Unit,
    ) {
        if (isAdLoading) {
            return
        }
        isAdLoading = true

        val adRequest = AdRequest.Builder().build()

        RewardedInterstitialAd.load(
            context,
            rewardedInterstitialAdUnitId,
            adRequest,
            rewardedInterstitialAdLoadCallback {
                showRewardedInterstitialAd(activity, onRewardedAd, adStates)
            }
        )
    }

    private fun showRewardedInterstitialAd(
        activity: Activity,
        onRewarded: (RewardItem) -> Unit,
        adStates: (AdStates) -> Unit,
    ) {

        if (rewardedInterstitialAd == null) {
            loadAndShowRewardedInterstitialAd(activity, onRewarded, adStates)
            return
        }
        if (isRewardedInterstitialShowing) {
            return
        }

        rewardedInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdShowedFullScreenContent() {
                isRewardedInterstitialShowing = true
                adStates(AdStates.AD_SHOWED)
            }

            override fun onAdDismissedFullScreenContent() {
                rewardedInterstitialAd = null
                isRewardedInterstitialShowing = false
                adStates(AdStates.AD_DISMISSED)
            }

            override fun onAdFailedToShowFullScreenContent(error: AdError) {
                rewardedInterstitialAd = null
                isRewardedInterstitialShowing = false
                adStates(AdStates.AD_FAILED_TO_SHOW)
            }
        }

        rewardedInterstitialAd?.show(activity) { rewardItem ->
            onRewarded(rewardItem)
        }

    }


}