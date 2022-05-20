package com.example.admodule.domain.repository

import com.facebook.ads.InterstitialAd
import com.facebook.ads.NativeAdLayout
import com.facebook.ads.NativeBannerAd

interface FacebookAdsRepository {

    suspend fun loadFbNativeAd(
        fbNativeLoaded: (nativeAd: NativeBannerAd?) -> Unit,
        fbNativeFailed: () -> Unit,
        fbImpression: () -> Unit
    )

    fun showFbNativeAd(
        nativeBannerAd: NativeBannerAd?,
    ): NativeAdLayout

    fun loadFbInterstitial(
        fbInterLoaded: (interAd: InterstitialAd?) -> Unit,
        fbInterFailed: () -> Unit,
        fbInterDismissed: () -> Unit
    )

}