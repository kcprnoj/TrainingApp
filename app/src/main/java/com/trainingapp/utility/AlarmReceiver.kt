package com.trainingapp.utility

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.provider.Settings.Global.getString
import android.util.Log

import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import com.example.main.R
import com.trainingapp.ui.MainActivity


class AlarmReceiver : BroadcastReceiver() {
    private val CHANNEL_ID = "0"

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("alarm_done", "alarm_call")

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_directions_run_24)
                .setContentTitle("Alarm")
                .setContentText("Time for run!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            notify(0, builder.build())
        }

    }


}