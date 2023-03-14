package com.example.dummychatapp.location

import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

class PermissionUtil(private val activity: FragmentActivity) {

    private var onGranted: () -> Unit = {}
    private var onDenied: () -> Unit = {}
    private var onShouldShowRationale: () -> Unit = {}

    private val launcher =
        activity.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            var isGranted = false

            for (result in it) {
                isGranted = result.value
            }
            if (isGranted)
                onGranted()
            else
                onDenied()
        }

    fun launch(
        permissions: Array<String>,
        onGranted: () -> Unit = {},
        onDenied: () -> Unit = {},
        onShouldShowRationale: () -> Unit = {}
    ) {
        this.onGranted = onGranted
        this.onDenied = onDenied
        this.onShouldShowRationale = onShouldShowRationale

        when {
            // You can use the API that requires the permission.
            permissions.checkSelfPermission() -> onGranted()
            // In an educational UI, explain to the user why your app requires this
            // permission for a specific feature to behave as expected. In this UI,
            // include a "cancel" or "no thanks" button that allows the user to
            // continue using your app without granting the permission.
            permissions.checkShouldShowRationale(activity) -> onShouldShowRationale()
            else -> launcher.launch(permissions)
        }
    }

    private fun Array<String>.checkSelfPermission(): Boolean {
        var isGranted = false
        this.forEach {
            isGranted = ContextCompat.checkSelfPermission(
                activity,
                it
            ) == PackageManager.PERMISSION_GRANTED

            //if at least one permission is not granted, stop checking
            if (!isGranted) return false
        }
        return isGranted
    }

    private fun Array<String>.checkShouldShowRationale(
        activity: FragmentActivity
    ): Boolean {
        this.forEach {
            val isShouldShow = ActivityCompat.shouldShowRequestPermissionRationale(activity, it)

            //if at least one permission should be rationale, stop checking
            if (isShouldShow) return true
        }
        return false
    }
}
