package com.example.admodule.data.repository

import android.app.Application
import android.view.LayoutInflater
import android.view.View
import com.example.admodule.R
import com.example.admodule.databinding.FbNativeBannerBinding
import com.example.admodule.domain.repository.FacebookAdsRepository
import com.facebook.ads.*
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class FacebookAdsRepositoryImpl
@Inject
constructor(
    private val appContext: Application
) : FacebookAdsRepository {

    override suspend fun loadFbNativeAd(
        fbNativeLoaded: (nativeAd: NativeBannerAd?) -> Unit,
        fbNativeFailed: () -> Unit,
        fbImpression: () -> Unit
    ) {

        val fbNativeBannerAd: NativeBannerAd?

        fbNativeBannerAd = NativeBannerAd(
            appContext,
            appContext.getString(R.string.fb_native_banner_id)
        )

        val nativeAdListener: NativeAdListener = object : NativeAdListener {

            override fun onMediaDownloaded(ad: Ad?) {}

            override fun onError(ad: Ad?, adError: AdError) {
                Timber.d("Fb Native banner failed: ${adError.errorCode}: ${adError.errorMessage}")
                fbNativeFailed()
            }

            override fun onAdLoaded(ad: Ad?) {
                Timber.d("Fb Native banner loaded")

                if (fbNativeBannerAd !== ad) {
                    return
                }

                fbNativeLoaded(fbNativeBannerAd)

            }

            override fun onAdClicked(ad: Ad?) {

            }

            override fun onLoggingImpression(ad: Ad?) {
                Timber.d("Fb Native banner Impression")
                fbImpression()
            }
        }

        fbNativeBannerAd.loadAd(
            fbNativeBannerAd.buildLoadAdConfig().withAdListener(nativeAdListener).build()
        )

    }


    override fun showFbNativeAd(
        nativeBannerAd: NativeBannerAd?,
    ): NativeAdLayout {
        nativeBannerAd?.unregisterView()

        FbNativeBannerBinding.inflate(
            LayoutInflater.from(appContext),
            null,
            false
        ).apply {
            val adOptionsView = AdOptionsView(
                appContext,
                nativeBannerAd,
                nativeAdLayout
            )
            nativeAdCallToAction.text = nativeBannerAd?.adCallToAction
            nativeAdCallToAction.visibility =
                if (nativeBannerAd?.hasCallToAction() == true) View.VISIBLE
                else View.INVISIBLE
            nativeAdTitle.text = nativeBannerAd?.advertiserName
            nativeAdSocialContext.text = nativeBannerAd?.adSocialContext
            nativeAdSponsoredLabel.text = nativeBannerAd?.sponsoredTranslation

            val clickableViews: MutableList<View?> = ArrayList()
            clickableViews.add(nativeAdTitle)
            clickableViews.add(nativeAdCallToAction)

            adChoicesContainer.removeAllViews()
            adChoicesContainer.addView(adOptionsView, 0)

            nativeBannerAd?.registerViewForInteraction(
                nativeAdLayout,
                nativeIconView,
                clickableViews
            )

            return this.root
        }

    }

    override fun loadFbInterstitial(
        fbInterLoaded: (interAd: InterstitialAd?) -> Unit,
        fbInterFailed: () -> Unit,
        fbInterDismissed: () -> Unit
    ) {

        val interstitialAd =
            InterstitialAd(
                appContext,
                appContext.getString(R.string.fb_interstitial_id)
            )

        val interstitialAdListener: InterstitialAdListener = object : InterstitialAdListener {
            override fun onInterstitialDisplayed(ad: Ad) {
                Timber.d("Interstitial ad displayed.")
            }

            override fun onInterstitialDismissed(ad: Ad) {
                fbInterDismissed()
                Timber.d("Interstitial ad dismissed.")
            }

            override fun onError(ad: Ad?, adError: AdError) {
                fbInterFailed()
                Timber.d("Interstitial ad failed to load: " + adError.errorMessage)
            }

            override fun onAdLoaded(ad: Ad) {
                Timber.d("Interstitial ad is loaded and ready to be displayed!")
                fbInterLoaded(
                    interstitialAd
                )
            }

            override fun onAdClicked(ad: Ad) {
                Timber.d("Interstitial ad clicked!")
            }

            override fun onLoggingImpression(ad: Ad) {
                Timber.d("Interstitial ad impression logged!")
            }
        }

        interstitialAd.loadAd(
            interstitialAd.buildLoadAdConfig()
                .withAdListener(interstitialAdListener)
                .build()
        )

    }

}