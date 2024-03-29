package com.trainingapp.viewmodels.tracking

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.observe
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class TrackingLifecycle : LifecycleService() {

    private var isFirst = true
    private var service = true
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var request: LocationRequest
    
    companion object {
        val timeInMillis = MutableLiveData<Long>()
        val isTracking = MutableLiveData<Boolean>()
        val pathPoints = MutableLiveData<MutableList<MutableList<LatLng>>>()
    }

    private var isTimerEnabled = false
    private var lapTime = 0L
    private var timeRun = 0L
    private var timeStarted = 0L

    private fun init() {
        isTracking.postValue(false)
        pathPoints.postValue(mutableListOf())
        timeInMillis.postValue(0L)
    }

    override fun onCreate() {
        super.onCreate()
        init()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        isTracking.observe(this) {
            update(it)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        when (intent?.action) {
            "START_OR_RESUME" -> {
                if (isFirst) {
                    startForegroundService()
                    isFirst = false
                    Log.d("TrackingService", "Started Service")
                } else {
                    startTimer()
                    Log.d("TrackingService", "Resumed Service")
                }
            }
            "STOP" -> {
                Log.d("TRACKING SERVICE", "STOPPED")
                killService()
            }
            "PAUSE" -> {
                Log.d("TRACKING SERVICE", "PAUSED")
                pauseService()
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)
            if (isTracking.value!!) {
                result.locations.let { locations ->
                    for (location in locations) {
                        addPathPoint(location)
                        Log.d("TrackingService", "Added path point ${location.latitude} ${location.longitude}")
                    }
                }
            }
        }
    }

    private fun addPathPoint(location: Location?) {
        location?.let {
            val pos = LatLng(location.latitude, location.longitude)
            pathPoints.value?.apply {
                last().add(pos)
                pathPoints.postValue(this)
            }
        }
    }

    private fun addEmptyList() = pathPoints.value?.apply {
        add(mutableListOf())
        pathPoints.postValue(this)
    } ?: pathPoints.postValue((mutableListOf(mutableListOf())))

    private  fun update(isTracking: Boolean) {
        if (isTracking) {
            val request = LocationRequest.create().apply {
                interval = 5000L
                fastestInterval = 3000L
                priority = PRIORITY_HIGH_ACCURACY
            }
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }

            fusedLocationProviderClient.requestLocationUpdates(
                request,
                locationCallback,
                Looper.getMainLooper()
            )
        } else {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }

    private fun startForegroundService() {
        startTimer()
        isTracking.postValue(true)
    }

    private fun pauseService() {
        isTracking.postValue(false)
        isTimerEnabled = false
    }

    private fun killService() {
        service = false
        isFirst = true
        pauseService()
        init()
        stopForeground(true)
        stopSelf()
    }

    private fun startTimer() {
        addEmptyList()
        isTracking.postValue(true)
        timeStarted = System.currentTimeMillis()
        isTimerEnabled = true
        CoroutineScope(Dispatchers.Main).launch {
            while (isTracking.value!!) {
                lapTime = System.currentTimeMillis() - timeStarted
                timeInMillis.postValue(timeRun + lapTime)
                delay(200L)
            }
            timeRun += lapTime
        }
    }
}
