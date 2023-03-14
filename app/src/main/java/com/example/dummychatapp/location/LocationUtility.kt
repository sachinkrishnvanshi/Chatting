package com.example.dummychatapp.location

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Looper
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch


@Suppress("WildcardImport")
open class LocationUtility(private val fragment: FragmentActivity) :
    DefaultLifecycleObserver {


    // FusedLocationProviderClient - Main class for receiving location updates.
    private var mLocationRequest: LocationRequest? = null


    // FusedLocationProviderClient - Main class for receiving location updates.
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null

    // This will store current location info
    var currentLocation = MutableLiveData<Pair<Double?, Double?>>()

    init {
        fusedLocationProviderClient = LocationServices
            .getFusedLocationProviderClient(fragment)
    }

    /**
     * start location updates
     */
    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        startLocationClient()
    }

    /**
     * call this method when you start the location client
     */
    fun startLocationClient() {
        // Kick off the process of building the GoogleApiClient, LocationRequest, and
        // LocationSettingsRequest objects.
        initLocationRequest()
        checkLocationSettings()
    }

    /**
     * initializing location request
     */
    private fun initLocationRequest() {
        mLocationRequest = LocationRequest
            .Builder(UPDATE_INTERVAL_IN_MILLISECONDS)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .setIntervalMillis(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS)
            .build()
    }

    /**
     * Check if the device's location settings are adequate for the app's needs using the
     * [com.google.android.gms.location.SettingsApi.checkLocationSettings] method,
     * with the results provided through a `PendingResult`.
     */
    private fun checkLocationSettings() {
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest!!)
        builder.setAlwaysShow(true)

        val locationSettingTask = LocationServices.getSettingsClient(fragment)
            .checkLocationSettings(builder.build())

        locationSettingTask.addOnSuccessListener {
            startLocationUpdates()
        }

        locationSettingTask.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    val request: IntentSenderRequest = IntentSenderRequest.Builder(
                        exception.resolution.intentSender
                    ).setFillInIntent(Intent())
                        .build()

                    registration.launch(request)
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }
    }

    /**
     * Requests location updates from the FusedLocationApi.
     */
    @SuppressLint("MissingPermission")
    fun startLocationUpdates() {
        when (checkPermissions()) {
            true -> {
                mLocationRequest?.let {
                    fusedLocationProviderClient?.requestLocationUpdates(
                        it,
                        locationCallback,
                        Looper.getMainLooper()
                    )
                }
            }
            false -> {
            }
        }

    }

    /**
     * Initialize locationCallback
     */
    private val locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            val latitude = locationResult.locations[0]?.latitude
            val longitude = locationResult.locations[0]?.longitude
            val locationInfo = Pair(latitude, longitude)
            currentLocation.value = locationInfo
        }
    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    private fun stopLocationUpdates() {
        fusedLocationProviderClient?.removeLocationUpdates(locationCallback)
    }

    /**
     * stop the location updates
     */
    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        stopLocationUpdates()
    }

    /**
     * check location permission is granted or not
     */
    private fun checkPermissions(): Boolean {
        if (
            ActivityCompat.checkSelfPermission(
                fragment, Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                fragment, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    /**
     * get callback of gps dialog response
     */
    private val registration: ActivityResultLauncher<IntentSenderRequest> =
        fragment.registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) {
            when (it.resultCode) {
                Activity.RESULT_OK -> {
                    startLocationUpdates()
                }
                Activity.RESULT_CANCELED -> {
                }
            }
        }


    fun isGpsEnable(): Boolean {
        val manager =
            fragment.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    @SuppressLint("MissingPermission")
    fun getOneTime() = callbackFlow {
        fusedLocationProviderClient?.let {
            it.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, object : CancellationToken() {

                override fun onCanceledRequested(listener: OnTokenCanceledListener) =
                    CancellationTokenSource().token

                override fun isCancellationRequested() = false
            })
                .addOnSuccessListener { currentCoordinate ->
                    currentCoordinate?.let {
                        val lat = currentCoordinate.latitude
                        val log = currentCoordinate.longitude
                        launch { send(Pair(lat, log)) }
                    }
                }
                .addOnFailureListener {
                    launch { send(null) }
                }
                .addOnCanceledListener {
                    launch { send(null) }
                }
            awaitClose {}
        }
    }.distinctUntilChanged()

    companion object {
        /**
         * Constant used in the location settings dialog.
         */
        const val REQUEST_CHECK_SETTINGS = 0x1

        /**
         * The desired interval for location updates. Inexact. Updates may be more or less frequent.
         */
        const val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 100

        /**
         * The fastest rate for active location updates. Exact. Updates will never be more frequent
         * than this value.
         */
        const val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2
    }

}
