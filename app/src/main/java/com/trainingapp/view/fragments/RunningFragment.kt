package com.trainingapp.view.fragments

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.example.main.R
import com.example.main.databinding.FragmentRunningBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapsSdkInitializedCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.trainingapp.RunApplication
import com.trainingapp.model.data.TrainingCreate
import com.trainingapp.model.repository.TrainingRepository
import com.trainingapp.model.webservice.TrainingService
import com.trainingapp.view.MainActivity
import com.trainingapp.viewmodels.RunningViewModel
import com.trainingapp.viewmodels.RunningViewModelFactory
import com.trainingapp.viewmodels.tracking.TrackingLifecycle
import kotlinx.android.synthetic.main.fragment_running.*
import java.util.*

class RunningFragment : Fragment(), OnMapsSdkInitializedCallback {
    private lateinit var binding: FragmentRunningBinding
    private lateinit var viewModel : RunningViewModel
    private lateinit var viewModelFactory: RunningViewModelFactory

    private var map: GoogleMap? = null

    private var isTracking = false
    private var pathPoints = mutableListOf<MutableList<LatLng>>()

    private var currentTime = 0L
    private var distance = 0
    private var start: Long = 0L
    private var calories = 0
    private var weight = 60f

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_running,container,false)
        val perf = (requireActivity().application as RunApplication).perfRepository
        viewModelFactory = RunningViewModelFactory(
            TrainingRepository(TrainingService(), perf), (requireActivity().application
                as RunApplication).perfRepository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(RunningViewModel::class.java)

        val repo = (requireActivity().application as RunApplication).perfRepository

        weight = repo.getUser().weight.toFloat()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let { MapsInitializer.initialize(it, MapsInitializer.Renderer.LATEST, this) }
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
        val intent = Intent(requireContext(), TrackingLifecycle::class.java)
        intent.action = action
        requireContext().startService(intent)
    }

    private fun subToObservers() {
        TrackingLifecycle.isTracking.observe(viewLifecycleOwner) {
            updateTracking(it)
        }

        TrackingLifecycle.pathPoints.observe(viewLifecycleOwner) {
            pathPoints = it
            addLatestLocation()
            moveCamera()
        }

        TrackingLifecycle.timeInMillis.observe(viewLifecycleOwner) {
            currentTime = it
            val formattedTime = formatTime(currentTime)
            time_display.text = formattedTime

            updateDistance()
            updateCalories()
            val distanceInKm = distance/1000.0
            distance_display.text = "$distanceInKm km"
            calories_display.text = "$calories kcal"
        }
    }

    private fun toggleRun() {
        if(isTracking) {
            sendCommandToService("PAUSE")
        } else {
            sendCommandToService("START_OR_RESUME")
            if (start == 0L) {
                start = Calendar.getInstance().timeInMillis
            }
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

        val run = TrainingCreate(
                        start,
                        Calendar.getInstance().timeInMillis,
                distance/1000.0,
                        currentTime,
                        "",
                        "")

        viewModel.addTraining(run)
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
            distance += calculateDistance(locations).toInt()
        }
    }

    private fun updateCalories() {
        calories = calculateCalories(distance.toDouble(), weight)
    }

    override fun onMapsSdkInitialized(renderer: MapsInitializer.Renderer) {
        Log.d("Map: ", renderer.toString())
    }


    fun calculateDistance(polyline: MutableList<LatLng>): Float {
        var distance = 0f
        for(i in 0..polyline.size - 2) {
            val pos1 = polyline[i]
            val pos2 = polyline[i + 1]

            val result = FloatArray(1)
            Location.distanceBetween(
                pos1.latitude,
                pos1.longitude,
                pos2.latitude,
                pos2.longitude,
                result
            )
            distance += result[0]
        }
        return distance
    }

    private fun calculateCalories(distance: Double, weight: Float): Int{
        return (distance.toFloat()/1000f*weight*0.8).toInt()
    }

    private fun formatTime(time: Long): String {
        var seconds = time/1000
        var minutes = seconds/60
        var hours = minutes/60

        seconds %= 60
        minutes %= 60
        hours %= 60

        val secondsString: String = if (seconds < 10)
            "0$seconds"
        else
            "$seconds"

        val minutesString: String = if (minutes < 10)
            "0$minutes"
        else
            "$minutes"

        val hoursString : String = if (hours < 10)
            "0$hours"
        else
            "$hours"

        return "$hoursString:$minutesString:$secondsString"
    }
}