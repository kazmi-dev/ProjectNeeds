import android.content.Context
import com.cooptech.pdfreader.applicationClass.ApplicationClass
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AdsModule {

    @Provides
    @Singleton
    fun providesInterstitialAdManagerInstance(@ApplicationContext context: Context): InterstitialAdManager{
        return InterstitialAdManager(context)
    }

    @Provides
    @Singleton
    fun providesAppOpenAdManagerInstance(@ApplicationContext context: Context): AppOpenAdManager {
        return AppOpenAdManager(context as ApplicationClass)
    }

    @Provides
    @Singleton
    fun providesRewardedAdManagerInstance(@ApplicationContext context: Context): RewardedAdManager {
        return RewardedAdManager(context)
    }

    @Provides
    @Singleton
    fun providesRewardedInterstitialAdManagerInstance(@ApplicationContext context: Context): RewardedInterstitialAdManager {
        return RewardedInterstitialAdManager(context)
    }


    @Provides
    @Singleton
    fun providesNativeAdInstance(@ApplicationContext context: Context): NativeAdsManager {
        return NativeAdsManager(context)
    }

}
