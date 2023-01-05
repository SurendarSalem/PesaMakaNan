package com.asmirestoran.pesomakanan

import android.content.Context
import android.content.Intent
import android.os.Bundle

object NavigationUtils {
    fun <T> openActivity(context: Context, cls: Class<T>, bundle: Bundle? = null) {
        val intent = Intent(context, cls)
        bundle?.let {
            intent.putExtras(bundle)
        }
        context.startActivity(intent)
    }
}