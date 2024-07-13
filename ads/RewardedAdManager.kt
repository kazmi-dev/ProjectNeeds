package com.capra.android.auto.carplay.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import com.capra.android.auto.carplay.R
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

class RewardedAdManager(private val context: Context) {

    private val REWARDED_LOG = "23438578843758934445"
    private var rewardedAd: RewardedAd? = null
    private var isRewardedAdShowing = false


    fun loadRewardedAd(activity: Activity, onRewarded: (RewardItem) -> Unit) {
        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(
            context,
            context.getString(R.string.interstitial_rewarded),
            adRequest,
            object: RewardedAdLoadCallback(){

                override fun onAdLoaded(ad: RewardedAd) {
                    rewardedAd = ad
                    Log.d(REWARDED_LOG, "rewarded ad loaded")
                    showRewardedAd(activity, onRewarded)
                }

                override fun onAdFailedToLoad(loadError: LoadAdError) {
                    rewardedAd = null
                    Log.d(REWARDED_LOG, "failed to load rewarded ad: ${loadError.message}")
                }
            }
        )
    }

    fun showRewardedAd(activity: Activity, onRewarded: (RewardItem) -> Unit){
        if(rewardedAd == null){
            Log.d(REWARDED_LOG, "rewarded ad is null")
            loadRewardedAd(activity, onRewarded)
            return
        }
        if(isRewardedAdShowing){
            Log.d(REWARDED_LOG, "rewarded ad is showing")
            return
        }

        rewardedAd?.fullScreenContentCallback = object: FullScreenContentCallback(){
            override fun onAdShowedFullScreenContent() {
                isRewardedAdShowing = true
            }

            override fun onAdDismissedFullScreenContent() {
                Log.d(REWARDED_LOG, "rewarded ad on dismiss")
                isRewardedAdShowing = false
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                Log.d(REWARDED_LOG, "adError: ${adError.message}")
            }

        }

        rewardedAd?.show(activity) {
            Log.d(REWARDED_LOG, "rewardType: ${it.type}")
            Log.d(REWARDED_LOG, "rewardAmount: ${it.amount}")
            onRewarded(it)
        }

    }

}