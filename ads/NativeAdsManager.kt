import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.cooptech.pdfreader.R
import com.google.android.ads.nativetemplates.TemplateView
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest

class NativeAdsManager(private val context: Context) {

    private var nativeAdUnitId = context.getString(R.string.native_ad_id)

    fun setNativeAdUnitId(adUnitId: String){
        nativeAdUnitId = adUnitId
    }

    fun initNativeAds(templateView: TemplateView, loadingView: ConstraintLayout){

        val adLoader = AdLoader.Builder(context, nativeAdUnitId).forNativeAd {nativeAd->

            //set native ad
            templateView.setNativeAd(nativeAd)

            //set visibility
            templateView.isVisible = true
            loadingView.isVisible = false

        }.build()

        val adRequest = AdRequest.Builder().build()

        adLoader.loadAd(adRequest)

    }

}
