package com.asmirestoran.pesomakanan

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment


class PermissionUtils(val permissions: Array<String>, val requestCode: Int, val fragment: Fragment) {
    fun requestPermission() {
    }

    private val requestPermissionLauncher: ActivityResultLauncher<String> =
        fragment.registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            if (result) {
                // PERMISSION GRANTED
            } else {
                // PERMISSION NOT GRANTED
            }
        }
}