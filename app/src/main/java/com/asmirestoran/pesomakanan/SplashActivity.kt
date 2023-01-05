package com.asmirestoran.pesomakanan

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.asmirestoran.pesomakanan.local.PreferenceHelper
import com.asmirestoran.pesomakanan.ui.AuthenticationActivity
import com.asmirestoran.pesomakanan.ui.HomeActivity
import com.asmirestoran.pesomakanan.ui.OrderActivity


const val SPLASH_SCREEN_DURATION = 3000L

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class SplashActivity : Activity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        Handler(Looper.getMainLooper()).postDelayed({
            val intent: Intent
            if (PreferenceHelper.isLogin) {
                intent = Intent(this, OrderActivity::class.java)
            } else {
                intent = Intent(this, AuthenticationActivity::class.java)
            }
            startActivity(intent)
            finish()
        }, SPLASH_SCREEN_DURATION)
    }
}