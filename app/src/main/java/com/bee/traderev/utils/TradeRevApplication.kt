package com.bee.traderev.utils

import android.app.Application
import com.squareup.leakcanary.LeakCanary

class TradeRevApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        application = this
//        initializeLeakCanary()
    }
    private fun initializeLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return
        }
        LeakCanary.install(this)
    }

    companion object {
        var application: TradeRevApplication? = null
    }
}