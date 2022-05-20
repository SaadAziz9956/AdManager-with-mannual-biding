package com.example.admodule.ui.screens

import android.widget.FrameLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.admodule.ui.MainActivity
import com.example.admodule.ui.viewmodel.AdsViewModel

@Composable
fun AdsScreen(
    adsViewModel: AdsViewModel = hiltViewModel()
) {

    Column(Modifier.fillMaxSize()) {

        NativeBannerScreen(adsViewModel)

    }


}

@Composable
fun NativeBannerScreen(adsViewModel: AdsViewModel) {

    var showNativeAd by remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current

    val state = adsViewModel.interAdsState.collectAsState()

    val adMobInter = state.value.adMobInter

    val fbInter = state.value.fbInter

    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { adsViewModel.loadNativeBanner() },
            modifier = Modifier.padding(top = 20.dp)
        ) {
            Text(text = "Load Native Banner Ad")
        }
    }


    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { showNativeAd = !showNativeAd },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text(text = "Show Native Banner Ad")
        }
    }

    Row(
        Modifier
            .fillMaxWidth()
            .height(115.dp)
            .padding(
                vertical = 15.dp,
                horizontal = 10.dp
            )
            .background(
                color = Color.Black
            ),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showNativeAd) {
            AndroidView(
                factory = { context ->
                    FrameLayout(context).apply {
                        adsViewModel.showNativeBanner(this)
                    }
                }, modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }

    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = { adsViewModel.loadInterstitialAd() },
            modifier = Modifier.padding(top = 20.dp)
        ) {
            Text(text = "Load Interstitial")
        }
    }


    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {
                adsViewModel.showInterstitialAd()
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text(text = "Show Interstitial")
        }
    }

    when{

        adMobInter != null -> {
            adMobInter.show(context as MainActivity)
            adsViewModel.resetValues()
        }

        fbInter != null -> {
            fbInter.show()
            adsViewModel.resetValues()
        }

    }


}
