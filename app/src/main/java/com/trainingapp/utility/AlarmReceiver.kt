package com.trainingapp.utility

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.main.R

class AlarmReceiver() : BroadcastReceiver() {
    private val channelID = "0"

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("alarm_done", "alarm_call")
        val title = intent.extras?.getString("title")
        val text = intent.extras?.getString("text")

        val builder = NotificationCompat.Builder(context, channelID)
                .setSmallIcon(R.drawable.baseline_directions_run_24)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            notify(0, builder.build())
        }

    }
}