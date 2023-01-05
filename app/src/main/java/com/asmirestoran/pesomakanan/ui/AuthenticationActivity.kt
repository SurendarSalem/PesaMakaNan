package com.asmirestoran.pesomakanan.ui

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.asmirestoran.pesomakanan.R
import com.asmirestoran.pesomakanan.local.PreferenceHelper
import com.asmirestoran.pesomakanan.ui.adapter.permissions


class AuthenticationActivity : AppCompatActivity() {
    lateinit var navController: NavController
    lateinit var progressBar: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)
        progressBar = findViewById(R.id.pb)
        initNavController()
        if (PreferenceHelper.isLogin) {
            val bundle = Bundle()
            navController.navigate(R.id.loginFragment, bundle)
        }
    }

    private fun initNavController() {
        val toolbar: Toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.elevation = 0f
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);
        //supportActionBar?.setTitle("Suren")
        supportActionBar?.setDisplayShowHomeEnabled(true);

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = setOf(),
            fallbackOnNavigateUpListener = ::onSupportNavigateUp
        )
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            run {
                if (destination.id == R.id.loginFragment) {
                    supportActionBar?.title = "Welcome to PesaMakaNan!"
                } else if (destination.id == R.id.signupFragment) {
                    supportActionBar?.title = "Get Started!"
                }
            }
        }
        setupWithNavController(
            toolbar,
            navController,
            appBarConfiguration
        )
    }

    fun showProgress(show: Boolean) {
        if (show) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_authentication)
    }
}