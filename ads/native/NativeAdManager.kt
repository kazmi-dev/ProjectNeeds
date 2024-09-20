import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import com.capra.live.hdwallpaper.R
import com.capra.live.hdwallpaper.databinding.NativeMediumAdViewBinding
import com.capra.live.hdwallpaper.databinding.NativeSmallMediumAdViewBinding
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions

class NativeAdManager(
    private val context: Context,
    private val layout: NativeTemplates,
    private val container: FrameLayout,
    private val isDark: Boolean = false
) {

    init {
        initNativeAd()
    }

    private fun initNativeAd() {

        val adLoader = AdLoader.Builder(context, context.getString(R.string.native_ad_id))
            .forNativeAd { nativeAd ->

                when (layout) {
                    NativeTemplates.MEDIUM_SMALL -> {
                        val binding =
                            NativeSmallMediumAdViewBinding.inflate(LayoutInflater.from(context))
                        populateMediumSmallAd(nativeAd, binding, container, isDark)
                    }

                    else -> {
                        val binding =
                            NativeMediumAdViewBinding.inflate(LayoutInflater.from(context))
                        populateMediumAd(nativeAd, binding, container, isDark)
                    }
                }

            }
            .withNativeAdOptions(NativeAdOptions.Builder().build())
            .build()

        val adRequest = AdRequest.Builder().build()
        adLoader.loadAd(adRequest)
    }

    private fun populateMediumAd(
        nativeAd: NativeAd,
        binding: NativeMediumAdViewBinding,
        container: FrameLayout,
        isDark: Boolean
    ) {
        binding.apply {
            if (isDark) {
                nativeAdView.background =
                    ResourcesCompat.getDrawable(context.resources, R.drawable.app_dark_bg, null)
                headlineTv.setTextColor(
                    ResourcesCompat.getColor(
                        context.resources,
                        R.color.app_light_bg,
                        null
                    )
                )
                bodyTv.setTextColor(
                    ResourcesCompat.getColor(
                        context.resources,
                        R.color.app_light_bg,
                        null
                    )
                )
            } else {
                nativeAdView.background =
                    ResourcesCompat.getDrawable(context.resources, R.drawable.app_light_bg, null)
                headlineTv.setTextColor(
                    ResourcesCompat.getColor(
                        context.resources,
                        R.color.app_dark_bg,
                        null
                    )
                )
                bodyTv.setTextColor(
                    ResourcesCompat.getColor(
                        context.resources,
                        R.color.app_dark_bg,
                        null
                    )
                )
            }

            val advertiserStoreText: String?

            //mediaView
            nativeAdView.mediaView = mediaView
            mediaView.mediaContent = nativeAd.mediaContent

            //headline
            nativeAdView.headlineView = headline
            if (nativeAd.headline == null) {
                headline.visibility = View.VISIBLE
                headlineTv.text = ""
            } else {
                headlineTv.text = nativeAd.headline
            }

            // Body
            nativeAdView.bodyView = bodyTv
            if (nativeAd.body == null) {
                bodyTv.visibility = View.INVISIBLE
            } else {
                bodyTv.visibility = View.VISIBLE
                bodyTv.text = nativeAd.body
            }

            // Call to Action
            nativeAdView.callToActionView = callToActionBtn
            if (nativeAd.callToAction == null) {
                callToActionBtn.visibility = View.INVISIBLE
            } else {
                callToActionBtn.visibility = View.VISIBLE
                callToActionBtn.text = nativeAd.callToAction
            }

            // Icon
            nativeAdView.iconView = icon
            if (nativeAd.icon == null) {
                icon.visibility = View.GONE
            } else {
                icon.setImageDrawable(nativeAd.icon?.drawable)
                icon.visibility = View.VISIBLE
            }

            // Advertiser and Store View
            if (hasOnlyStore(nativeAd)) {
                nativeAdView.storeView = advertiserStoreView
                advertiserStoreText = nativeAd.store
                advertiserStoreView.text = advertiserStoreText
            } else if (nativeAd.advertiser != null) {
                nativeAdView.advertiserView = advertiserStoreView
                advertiserStoreText = nativeAd.advertiser
                advertiserStoreView.text = advertiserStoreText
            } else {
                advertiserStoreText = ""
            }

            // Star Rating
            val rating: Double? = nativeAd.starRating
            if (rating != null && rating > 0) {
                advertiserStoreView.isVisible = false
                ratingBar.isVisible = true
                nativeAdView.starRatingView = ratingBar
                ratingBar.rating = nativeAd.starRating!!.toFloat()
            } else {
                advertiserStoreView.isVisible = true
                ratingBar.isVisible = false
                advertiserStoreView.text = advertiserStoreText
            }

            nativeAdView.setNativeAd(nativeAd)

        }

        // Attach the ad view to the provided container
        container.removeAllViews()
        container.addView(binding.root)
    }

    private fun populateMediumSmallAd(
        nativeAd: NativeAd,
        binding: NativeSmallMediumAdViewBinding,
        container: FrameLayout,
        isDark: Boolean
    ) {
        binding.apply {

            if (isDark) {
                nativeAdView.background =
                    ResourcesCompat.getDrawable(context.resources, R.drawable.app_dark_bg, null)
                headlineTv.setTextColor(
                    ResourcesCompat.getColor(
                        context.resources,
                        R.color.app_light_bg,
                        null
                    )
                )
                bodyTv.setTextColor(
                    ResourcesCompat.getColor(
                        context.resources,
                        R.color.app_light_bg,
                        null
                    )
                )
            } else {
                nativeAdView.background =
                    ResourcesCompat.getDrawable(context.resources, R.drawable.app_light_bg, null)
                headlineTv.setTextColor(
                    ResourcesCompat.getColor(
                        context.resources,
                        R.color.app_dark_bg,
                        null
                    )
                )
                bodyTv.setTextColor(
                    ResourcesCompat.getColor(
                        context.resources,
                        R.color.app_dark_bg,
                        null
                    )
                )
            }

            val advertiserStoreText: String?

            //mediaView
            nativeAdView.mediaView = mediaView
            mediaView.mediaContent = nativeAd.mediaContent

            //headline
            nativeAdView.headlineView = headline
            if (nativeAd.headline == null) {
                headline.visibility = View.VISIBLE
                headlineTv.text = ""
            } else {
                headlineTv.text = nativeAd.headline
            }

            // Body
            nativeAdView.bodyView = bodyTv
            if (nativeAd.body == null) {
                bodyTv.visibility = View.INVISIBLE
            } else {
                bodyTv.visibility = View.VISIBLE
                bodyTv.text = nativeAd.body
            }

            // Call to Action
            nativeAdView.callToActionView = callToActionBtn
            if (nativeAd.callToAction == null) {
                callToActionBtn.visibility = View.INVISIBLE
            } else {
                callToActionBtn.visibility = View.VISIBLE
                callToActionBtn.text = nativeAd.callToAction
            }

            // Icon
            nativeAdView.iconView = icon
            if (nativeAd.icon == null) {
                icon.visibility = View.GONE
            } else {
                icon.setImageDrawable(nativeAd.icon?.drawable)
                icon.visibility = View.VISIBLE
            }

            // Advertiser and Store View
            if (hasOnlyStore(nativeAd)) {
                nativeAdView.storeView = advertiserStoreView
                advertiserStoreText = nativeAd.store
                advertiserStoreView.text = advertiserStoreText
            } else if (nativeAd.advertiser != null) {
                nativeAdView.advertiserView = advertiserStoreView
                advertiserStoreText = nativeAd.advertiser
                advertiserStoreView.text = advertiserStoreText
            } else {
                advertiserStoreText = ""
            }

            // Star Rating
            val rating: Double? = nativeAd.starRating
            if (rating != null && rating > 0) {
                advertiserStoreView.isVisible = false
                ratingBar.isVisible = true
                nativeAdView.starRatingView = ratingBar
                ratingBar.rating = nativeAd.starRating!!.toFloat()
            } else {
                advertiserStoreView.isVisible = true
                ratingBar.isVisible = false
                advertiserStoreView.text = advertiserStoreText
            }

            nativeAdView.setNativeAd(nativeAd)
        }

        // Attach the ad view to the provided container
        container.removeAllViews()
        container.addView(binding.root)
    }

    private fun hasOnlyStore(nativeAd: NativeAd): Boolean {
        return nativeAd.store != null && nativeAd.advertiser == null
    }

}
