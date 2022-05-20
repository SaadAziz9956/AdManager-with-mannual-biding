package com.example.admodule.domain.repository

import android.widget.FrameLayout

interface AdsRepository {

    suspend fun loadAdMobNativeAd()

    fun showNativeAd(frameLayout: FrameLayout)

    fun loadFbNativeAd()

    suspend fun loadAdMobInterstitialAd()

    fun showInterstitialAd(): Any?

    suspend fun loadFbInterstitial()

}