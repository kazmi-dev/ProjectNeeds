import android.app.Activity
import android.content.Context
import android.util.Log
import com.capra.live.hdwallpaper.R
import com.capra.live.hdwallpaper.mvvm.enums.AdState
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
    private var isRewardedAdLoading = false


    fun loadRewardedAd(onLoaded:(isFailed:Boolean)->Unit) {
        if (rewardedAd != null){
            onLoaded(true)
            return
        }
        isRewardedAdLoading = true

        //request Ad
        val adRequest = AdRequest.Builder().build()

        //load Ad
        RewardedAd.load(
            context,
            context.getString(R.string.rewarded_ad),
            adRequest,
            rewardAdLoadCallback(onLoaded)
        )
    }

    private fun rewardAdLoadCallback(onLoaded:(isLoaded:Boolean)->Unit) = object: RewardedAdLoadCallback(){
        override fun onAdLoaded(ad: RewardedAd) {
            Log.d(REWARDED_LOG, "rewarded ad loaded")
            isRewardedAdLoading = false
            rewardedAd = ad
            onLoaded(true)
        }
        override fun onAdFailedToLoad(loadError: LoadAdError) {
            Log.d(REWARDED_LOG, "failed to load rewarded ad: ${loadError.message}")
            isRewardedAdLoading = false
            rewardedAd = null
            onLoaded(false)
        }
    }

    fun showRewardedAd(activity: Activity, callback: (AdState) -> Unit, onReward: (RewardItem) -> Unit){

        //check is reward showing
        if(isRewardedAdShowing || isRewardedAdLoading){
            Log.d(REWARDED_LOG, "rewarded ad is showing or loading.")
            return
        }
        //check if reward ad is available
        if(rewardedAd == null){
            Log.d(REWARDED_LOG, "rewarded ad is null")
            callback(AdState.IS_NOT_LOADED)
            loadRewardedAd{}
            return
        }

        rewardedAd?.fullScreenContentCallback = object: FullScreenContentCallback(){
            override fun onAdShowedFullScreenContent() {
                isRewardedAdShowing = true
                callback(AdState.IS_SHOWED)
            }

            override fun onAdDismissedFullScreenContent() {
                Log.d(REWARDED_LOG, "rewarded ad on dismiss")
                isRewardedAdShowing = false
                rewardedAd = null
                callback(AdState.IS_DISMISSED)
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                Log.d(REWARDED_LOG, "adError: ${adError.message}")
                rewardedAd = null
                callback(AdState.IS_FAILED_TO_LOAD)
            }

        }

        rewardedAd?.show(activity) {
            Log.d(REWARDED_LOG, "rewardType: ${it.type}")
            Log.d(REWARDED_LOG, "rewardAmount: ${it.amount}")
            onReward(it)
        }

    }

}
