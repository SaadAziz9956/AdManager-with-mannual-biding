package com.example.admodule.domain.repository

import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView


interface AdMobRepository {

    suspend fun loadNativeBanner(
        adMobNativeLoaded: (nativeAd: NativeAd?) -> Unit,
        adMobNativeFailed: () -> Unit,
        adMobImpression: () -> Unit
    )

    fun showAdMobNativeAd(
        nativeAd: NativeAd
    ): NativeAdView

    fun loadAdMobInterstitial(
        adMobInterstitialLoaded: (nativeAd: InterstitialAd?) -> Unit,
        adMobInterstitialFailed: () -> Unit,
        adMobInterstitialDismissed: () -> Unit
    )

}
