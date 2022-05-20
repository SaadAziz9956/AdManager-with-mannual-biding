package com.example.admodule.data.repository

import android.app.Application
import android.widget.FrameLayout
import android.widget.Toast
import com.example.admodule.domain.repository.AdMobRepository
import com.example.admodule.domain.repository.AdsRepository
import com.example.admodule.domain.repository.FacebookAdsRepository
import com.facebook.ads.NativeBannerAd
import com.google.android.gms.ads.nativead.NativeAd
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdsRepositoryImpl
@Inject
constructor(
    private val adMob: AdMobRepository,
    private val fbAds: FacebookAdsRepository,
    private val appContext: Application
) : AdsRepository {

    private var nativeAdView: Any? = null

    private var interstitialAd: Any? = null

    override suspend fun loadAdMobNativeAd() {

        adMob.loadNativeBanner(

            adMobNativeFailed = {
                nativeAdView = null
                loadFbNativeAd()
                Toast.makeText(
                    appContext,
                    "AdMob native Ad failed",
                    Toast.LENGTH_SHORT
                ).show()
            },

            adMobNativeLoaded = { adMobNativeAd ->
                Toast.makeText(
                    appContext,
                    "AdMob native Ad loaded",
                    Toast.LENGTH_SHORT
                ).show()
                nativeAdView = adMobNativeAd
            },

            adMobImpression = {
                Toast.makeText(
                    appContext,
                    "AdMob native Ad Impression registered",
                    Toast.LENGTH_SHORT
                ).show()
                CoroutineScope(Dispatchers.Default).launch {
                    nativeAdView = null
                    loadAdMobNativeAd()
                }
            }

        )

    }

    override fun loadFbNativeAd() {

        CoroutineScope(Dispatchers.Default).launch {

            fbAds.loadFbNativeAd(
                fbNativeLoaded = { fbNativeAd ->
                    nativeAdView = fbNativeAd
                    Toast.makeText(
                        appContext,
                        "Facebook native Ad loaded",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                fbNativeFailed = {
                    nativeAdView = null
                    Toast.makeText(
                        appContext,
                        "Facebook native Ad failed",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                fbImpression = {
                    Toast.makeText(
                        appContext,
                        "Facebook native Ad Impression registered",
                        Toast.LENGTH_SHORT
                    ).show()
                    CoroutineScope(Dispatchers.Default).launch {
                        nativeAdView = null
                        loadAdMobNativeAd()
                    }
                }
            )

        }
    }

    override fun showNativeAd(frameLayout: FrameLayout) {

        when (nativeAdView) {

            is NativeAd -> {
                val showAdMobNativeAd = adMob.showAdMobNativeAd(nativeAdView as NativeAd)
                frameLayout.removeAllViews()
                frameLayout.addView(showAdMobNativeAd)
            }

            is NativeBannerAd -> {
                val showFbNativeAd = fbAds.showFbNativeAd(nativeAdView as NativeBannerAd)
                frameLayout.removeAllViews()
                frameLayout.addView(showFbNativeAd)
            }

            else -> {
                Toast.makeText(
                    appContext,
                    "Native view not found try to load Ad again.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    override suspend fun loadAdMobInterstitialAd() {

        adMob.loadAdMobInterstitial(
            adMobInterstitialLoaded = { adMobInterstitialAd ->
                Toast.makeText(
                    appContext,
                    "AdMob Interstitial ad Loaded",
                    Toast.LENGTH_SHORT
                ).show()
                interstitialAd = adMobInterstitialAd
            },
            adMobInterstitialFailed = {
                Toast.makeText(
                    appContext,
                    "AdMob Interstitial ad Failed",
                    Toast.LENGTH_SHORT
                ).show()
                CoroutineScope(Dispatchers.Main).launch {
                    loadFbInterstitial()
                }

            },
            adMobInterstitialDismissed = {
                Toast.makeText(
                    appContext,
                    "AdMob Interstitial ad dismissed",
                    Toast.LENGTH_SHORT
                ).show()
                CoroutineScope(Dispatchers.Main).launch {
                    interstitialAd = null
                    loadAdMobInterstitialAd()
                }
            }
        )

    }

    override suspend fun loadFbInterstitial() {

        fbAds.loadFbInterstitial(
            fbInterLoaded = { fbInterAd ->
                Toast.makeText(
                    appContext,
                    "Facebook Interstitial ad loaded",
                    Toast.LENGTH_SHORT
                ).show()
                interstitialAd = fbInterAd
            },
            fbInterFailed = {
                Toast.makeText(
                    appContext,
                    "Facebook Interstitial ad failed",
                    Toast.LENGTH_SHORT
                ).show()
                interstitialAd = null
            },
            fbInterDismissed = {
                Toast.makeText(
                    appContext,
                    "Facebook Interstitial ad dismissed",
                    Toast.LENGTH_SHORT
                ).show()
                CoroutineScope(Dispatchers.Main).launch {
                    interstitialAd = null
                    loadAdMobInterstitialAd()
                }
            }
        )

    }


    override fun showInterstitialAd(): Any? {

        return if (interstitialAd != null) {
            interstitialAd
        } else {
            Toast.makeText(
                appContext,
                "Interstitial ad wasn't ready yet.",
                Toast.LENGTH_SHORT
            ).show()
            null
        }

    }


}