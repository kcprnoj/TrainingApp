package com.trainingapp.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.main.R
import com.example.main.databinding.FragmentRunningBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolygonOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.trainingapp.RunApplication
import com.trainingapp.tracking.TrackingService
import com.trainingapp.viewmodels.RunningViewModel
import com.trainingapp.viewmodels.RunningViewModelFactory
import kotlinx.android.synthetic.main.fragment_running.*

class RunningFragment : Fragment() {
    private lateinit var binding: FragmentRunningBinding
    private lateinit var viewModel : RunningViewModel
    private lateinit var viewModelFactory: RunningViewModelFactory

    private var map: GoogleMap? = null

    private var isTracking = false;
    private var pathPoints = mutableListOf<MutableList<LatLng>>()

    private var currentTime = 0L

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_running,container,false)
        viewModelFactory = RunningViewModelFactory((activity?.application as RunApplication).repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(RunningViewModel::class.java)
        binding.runningViewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView.onCreate(savedInstanceState)
        start_stop.setOnClickListener{
            toggleRun()
        }

        mapView.getMapAsync {
            map = it
            addAllLocations()
        }
        subToObservers();
    }

    private fun sendCommandToService(action: String) {
        val intent = Intent(requireContext(), TrackingService::class.java)
        intent.action = action;
        requireContext().startService(intent)
    }

    private fun subToObservers() {
        TrackingService.isTracking.observe(viewLifecycleOwner, Observer {
            updateTracking(it)
        })

        TrackingService.pathPoints.observe(viewLifecycleOwner, Observer {
            pathPoints = it;
            addLatestLocation()
            moveCamera()
        })

        TrackingService.timeInMillis.observe(viewLifecycleOwner, Observer {
            currentTime = it
            val formattedTime = formatTime(currentTime)
            time_display.text = formattedTime
        })
    }

    private fun formatTime(time: Long): String {
        var seconds = time/1000
        var minutes = seconds/60
        var hours = minutes/60

        seconds %= 60
        minutes %= 60
        hours %= 60

        var secondsString = ""
        var minutesString = ""
        var hoursString = ""

        secondsString = if (seconds < 10)
            "0$seconds"
        else
            "$seconds"

        minutesString = if (minutes < 10)
            "0$minutes"
        else
            "$minutes"

        hoursString = if (hours < 10)
            "0$hours"
        else
            "$hours"

        return "$hoursString:$minutesString:$secondsString"
    }

    private fun toggleRun() {
        if(isTracking) {
            sendCommandToService("PAUSE")
        } else {
            sendCommandToService("START_OR_RESUME")
        }
    }

    private fun updateTracking(isTracking: Boolean) {
        this.isTracking = isTracking
        if (isTracking) {
            start_stop.text = "Stop"
            button_end.visibility = View.GONE
        } else {
            start_stop.text = "Start"
            button_end.visibility = View.VISIBLE
        }
    }

    private fun moveCamera() {
        if (pathPoints.isNotEmpty() && pathPoints.last().isNotEmpty()) {
            map?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    pathPoints.last().last(),
                    15f
                )
            )
        }
    }

    private fun addAllLocations() {
        for (pathPoint in pathPoints) {
            val polyline = PolylineOptions()
                .color(Color.BLUE)
                .width(8f)
                .addAll(pathPoint)
            map?.addPolyline(polyline)
        }
    }

    private fun addLatestLocation() {
        if (pathPoints.isNotEmpty() && pathPoints.last().size >= 2) {
            val preLng = pathPoints.last()[pathPoints.last().size -  2]
            val lastLng = pathPoints.last().last()
            val polyline = PolylineOptions()
                .color(Color.BLUE)
                .width(8f)
                .add(preLng)
                .add(lastLng)
            map?.addPolyline(polyline)
        }
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }
}