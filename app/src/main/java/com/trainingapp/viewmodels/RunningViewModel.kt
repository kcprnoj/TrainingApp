package com.trainingapp.viewmodels

import android.location.Location
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.trainingapp.model.data.TrainingCreate
import com.trainingapp.model.webservice.TrainingService

class RunningViewModel(private val service: TrainingService) : ViewModel() {


    fun addTraining(training: TrainingCreate, key: String) {
        service.addTraining(training, key)
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

    fun calculateCalories(distance: Double, weight: Float): Int{
        return (distance.toFloat()/1000f*weight*0.8).toInt()
    }

    fun formatTime(time: Long): String {
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
