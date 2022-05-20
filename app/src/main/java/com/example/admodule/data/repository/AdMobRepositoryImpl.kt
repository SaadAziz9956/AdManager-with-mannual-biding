package com.example.admodule.data.repository

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.admodule.R
import com.example.admodule.domain.repository.AdMobRepository
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdMobRepositoryImpl
@Inject
constructor(
    private val appContext: Application
) : AdMobRepository {

    override suspend fun loadNativeBanner(
        adMobNativeLoaded: (nativeAd: NativeAd?) -> Unit,
        adMobNativeFailed: () -> Unit,
        adMobImpression: () -> Unit
    ) {

        var nativeAd: NativeAd? = null

        val adLoader = AdLoader.Builder(appContext, appContext.getString(R.string.Admob_NativeId))
            .forNativeAd { ad: NativeAd ->
                nativeAd = ad
            }
            .withAdListener(object : AdListener() {

                override fun onAdLoaded() {
                    Timber.d("AdMob Native banner loaded")
                    adMobNativeLoaded(nativeAd)
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Timber.d("AdMob Native banner failed")
                    adMobNativeFailed()
                }

                override fun onAdImpression() {
                    super.onAdImpression()
                    Timber.d("AdMob Native banner Impression")
                    adMobImpression()
                }

            })
            .withNativeAdOptions(
                NativeAdOptions.Builder()
                    .build()
            ).build()

        adLoader.loadAd(AdRequest.Builder().build())

    }

    @SuppressLint("InflateParams")
    override fun showAdMobNativeAd(
        nativeAd: NativeAd
    ): NativeAdView {

        val inflater = appContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
                as LayoutInflater

        val nativeAdView = inflater.inflate(R.layout.native_banner, null, false) as NativeAdView

        nativeAdView.headlineView = nativeAdView.findViewById(R.id.primary)
        nativeAdView.bodyView = nativeAdView.findViewById(R.id.body)
        nativeAdView.callToActionView = nativeAdView.findViewById(R.id.cta)
        nativeAdView.iconView = nativeAdView.findViewById(R.id.icon)

        (nativeAdView.headlineView as TextView).text = nativeAd.headline

        (nativeAdView.bodyView as TextView).text = nativeAd.body

        if (nativeAd.callToAction == null) {
            nativeAdView.callToActionView?.visibility = View.INVISIBLE
        } else {
            nativeAdView.callToActionView?.visibility = View.VISIBLE
            (nativeAdView.callToActionView as Button).text = nativeAd.callToAction
        }

        if (nativeAd.icon == null) {
            nativeAdView.iconView?.visibility = View.GONE
        } else {
            (nativeAdView.iconView as ImageView).setImageDrawable(
                nativeAd.icon?.drawable
            )
            nativeAdView.iconView?.visibility = View.VISIBLE
        }

        nativeAdView.setNativeAd(nativeAd)

        return nativeAdView
    }

    override fun loadAdMobInterstitial(
        adMobInterstitialLoaded: (interstitialAd: InterstitialAd?) -> Unit,
        adMobInterstitialFailed: () -> Unit,
        adMobInterstitialDismissed: () -> Unit
    ) {

        var sInterstitialAd: InterstitialAd?

        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(appContext,
            appContext.getString(R.string.Admob_InterstitialId),
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Timber.d("AdMob Interstitial failed ${adError.message}")
                    adMobInterstitialFailed()
                    sInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    Timber.d("AdMob Interstitial was loaded.")
                    sInterstitialAd = interstitialAd
                    adMobInterstitialLoaded(
                        sInterstitialAd
                    )

                    interstitialAd.fullScreenContentCallback = object : FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {
                            Timber.d("AdMob Interstitial was dismissed.")
                            adMobInterstitialDismissed()
                            sInterstitialAd = null
                        }

                        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                            Timber.d("AdMob Interstitial was failed to show: ${adError.message}")
                            sInterstitialAd = null
                        }

                        override fun onAdShowedFullScreenContent() {
                            Timber.d("AdMob Interstitial showed fullscreen content.")
                            sInterstitialAd = null
                        }
                    }

                }
            })


    }

}

