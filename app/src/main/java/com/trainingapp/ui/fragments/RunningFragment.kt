package com.trainingapp.ui.fragments

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.example.main.R
import com.example.main.databinding.FragmentRunningBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.trainingapp.RunApplication
import com.trainingapp.db.Run
import com.trainingapp.tracking.TrackingService
import com.trainingapp.ui.MainActivity
import com.trainingapp.viewmodels.RunningViewModel
import com.trainingapp.viewmodels.RunningViewModelFactory
import kotlinx.android.synthetic.main.fragment_running.*
import java.util.*
import kotlin.math.round

class RunningFragment : Fragment() {
    private lateinit var binding: FragmentRunningBinding
    private lateinit var viewModel : RunningViewModel
    private lateinit var viewModelFactory: RunningViewModelFactory

    private var map: GoogleMap? = null

    private var isTracking = false
    private var pathPoints = mutableListOf<MutableList<LatLng>>()

    private var currentTime = 0L
    private var distance = 0
    private var calories = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_running,container,false)
        viewModelFactory = RunningViewModelFactory((activity?.application as RunApplication).repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(RunningViewModel::class.java)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView.onCreate(savedInstanceState)

        next_exercise.setOnClickListener{
            toggleRun()
        }

        button_end.setOnClickListener {
            button_end.visibility = View.GONE
            endRun()
        }

        mapView.getMapAsync {
            map = it
            addAllLocations()
        }
        subToObservers()
    }

    private fun sendCommandToService(action: String) {
        val intent = Intent(requireContext(), TrackingService::class.java)
        intent.action = action
        requireContext().startService(intent)
    }

    private fun subToObservers() {
        TrackingService.isTracking.observe(viewLifecycleOwner) {
            updateTracking(it)
        }

        TrackingService.pathPoints.observe(viewLifecycleOwner) {
            pathPoints = it
            addLatestLocation()
            moveCamera()
        }

        TrackingService.timeInMillis.observe(viewLifecycleOwner) {
            currentTime = it
            val formattedTime = viewModel.formatTime(currentTime)
            time_display.text = formattedTime

            updateDistance()
            updateCalories()
            val distanceInKm = distance.toDouble()/1000
            distance_display.text = "$distanceInKm km"
            calories_display.text = "$calories kcal"
        }
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
            next_exercise.text = getString(R.string.stop)
            button_end.visibility = View.GONE
        } else {
            next_exercise.text = getString(R.string.start)
            if (currentTime != 0L)
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

    private fun endRun() {
        updateDistance()
        updateCalories()
        val run = Run(
            Calendar.getInstance().timeInMillis,
            round((distance/1000f) / (currentTime/1000f/60f/60f)*10f)/10f,
            distance.toFloat(),
            currentTime,
            calories
        )
        viewModel.insert(run)
        sendCommandToService("STOP")
        button_end.visibility = View.GONE
        findNavController().navigate(RunningFragmentDirections.actionRunningFragmentToTrainingFragment())
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
        mapView?.onSaveInstanceState(outState)
    }

    private fun updateDistance() {
        distance = 0;
        for (locations in pathPoints) {
            distance += viewModel.calculateDistance(locations).toInt()
        }
    }

    private fun updateCalories() {
        val appSettingPrefs: SharedPreferences = (activity as MainActivity).getSharedPreferences("AppSettingPrefs",0)

        calories = viewModel.calculateCalories(distance, appSettingPrefs.getFloat("userWeight", 60.0f))
    }
}