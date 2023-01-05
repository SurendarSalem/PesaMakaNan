package com.asmirestoran.pesomakanan

import android.app.Application
import com.asmirestoran.pesomakanan.local.PreferenceHelper

class PesoMakaNanApp: Application() {

    override fun onCreate() {
        super.onCreate()
        PreferenceHelper.init(applicationContext)
    }
}