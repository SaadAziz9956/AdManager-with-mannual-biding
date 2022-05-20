package com.example.admodule

import android.app.Application
import com.facebook.ads.AudienceNetworkAds
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class App : Application() {

    var sTag = "saadTimberTags %s"

    override fun onCreate() {
        super.onCreate()

        initTimber()

        MobileAds.initialize(this)
        AudienceNetworkAds.initialize(this)

    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
                override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                    super.log(priority, String.format(sTag, tag), message, t)
                }
            })
        }
    }

}