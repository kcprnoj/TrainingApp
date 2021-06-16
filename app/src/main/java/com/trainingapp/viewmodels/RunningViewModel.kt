package com.trainingapp.viewmodels

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.trainingapp.db.Run
import com.trainingapp.repositories.RunRepository
import kotlinx.coroutines.launch

class RunningViewModel(private val repository: RunRepository) : ViewModel() {


    fun insert(run: Run) = viewModelScope.launch {
        repository.insert(run)
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

    fun calculateCalories(distance: Int, weight: Float): Int{
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
