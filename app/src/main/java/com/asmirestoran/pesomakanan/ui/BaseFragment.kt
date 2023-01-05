package com.asmirestoran.pesomakanan.ui

import android.view.View
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {

    fun showProgress(show: Boolean) {
        if (show) {
            getProgressBar().visibility = View.VISIBLE
        } else {
            getProgressBar().visibility = View.GONE
        }
    }

    abstract fun getProgressBar(): View
}