package com.example.admodule.ui.viewmodel

import android.widget.FrameLayout
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.admodule.domain.repository.AdsRepository
import com.google.android.gms.ads.interstitial.InterstitialAd
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class InterAdsStates (
    val adMobInter: InterstitialAd? = null,
    val fbInter: com.facebook.ads.InterstitialAd? = null
)

@HiltViewModel
class AdsViewModel
@Inject
constructor(
    private val adRepo: AdsRepository
) : ViewModel() {

    private var _interAdsState = MutableStateFlow(InterAdsStates())
    var interAdsState = _interAdsState.asStateFlow()


    fun loadNativeBanner() {
        viewModelScope.launch {
            adRepo.loadAdMobNativeAd()
        }
    }

    fun showNativeBanner(frameLayout: FrameLayout) {

        adRepo.showNativeAd(frameLayout)

    }

    fun loadInterstitialAd() {
        viewModelScope.launch {
            adRepo.loadAdMobInterstitialAd()
        }
    }

    fun showInterstitialAd() {

        viewModelScope.launch {
            when (val showInterstitialAd = adRepo.showInterstitialAd()) {

                is InterstitialAd -> {

                    _interAdsState.emit(
                        InterAdsStates(
                            adMobInter = showInterstitialAd
                        )
                    )

                }

                is com.facebook.ads.InterstitialAd -> {

                    _interAdsState.emit(
                        InterAdsStates(
                            fbInter = showInterstitialAd
                        )
                    )

                }

            }
        }

    }

    fun resetValues() {
        viewModelScope.launch {
            _interAdsState.emit(
                InterAdsStates(
                    fbInter = null,
                    adMobInter = null
                )
            )
        }
    }


}