import android.app.Activity
import android.content.Context
import com.cooptech.pdfreader.R
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

class RewardedAdManager(private val context: Context) {

    private var rewardedAd: RewardedAd? = null
    private var isAdLoading: Boolean = false
    private var rewardedAdUnitId = context.getString(R.string.rewarded_ad)
    private var isRewardedAdShowing: Boolean = false

    private fun rewardedAdLoadCallback(onLoad: () -> Unit) = object : RewardedAdLoadCallback() {
        override fun onAdLoaded(rewardedAd: RewardedAd) {
            isAdLoading = false
            this@RewardedAdManager.rewardedAd = rewardedAd
            onLoad()
        }

        override fun onAdFailedToLoad(error: LoadAdError) {
            isAdLoading = false
            rewardedAd = null
        }
    }


    fun setRewardedAdUnitId(rewardedAdUnitId: String) {
        this.rewardedAdUnitId = rewardedAdUnitId
    }

    fun loadAndShowRewardedAd(
        activity: Activity,
        onRewardedAd: (RewardItem) -> Unit,
        adStates: (AdStates) -> Unit,
    ) {
        //check if reward ad is not null
        if (rewardedAd != null) {
            return
        }

        //check if ad is already loading
        if (isAdLoading) {
            return
        }
        isAdLoading = true

        val adRequest = AdRequest.Builder().build()

        RewardedAd.load(
            context,
            rewardedAdUnitId,
            adRequest,
            rewardedAdLoadCallback {
                showRewardedAdIfAvailable(activity, onRewardedAd, adStates)
            }
        )
    }

    private fun showRewardedAdIfAvailable(
        activity: Activity,
        onRewarded: (RewardItem) -> Unit,
        adStates: (AdStates) -> Unit,
    ) {
        if (rewardedAd == null) {
            loadAndShowRewardedAd(activity, onRewarded, adStates)
            return
        }

        if (isRewardedAdShowing) {
            return
        }

        rewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {

            override fun onAdShowedFullScreenContent() {
                isRewardedAdShowing = true
                adStates(AdStates.AD_SHOWED)
            }

            override fun onAdDismissedFullScreenContent() {
                rewardedAd = null
                isRewardedAdShowing = false
                adStates(AdStates.AD_DISMISSED)
            }

            override fun onAdFailedToShowFullScreenContent(error: AdError) {
                rewardedAd = null
                isRewardedAdShowing = false
                adStates(AdStates.AD_FAILED_TO_SHOW)
            }

        }

        rewardedAd?.show(activity) {rewardItem->
            onRewarded(rewardItem)
        }

    }


}
