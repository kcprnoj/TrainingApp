package com.trainingapp.viewmodels

import android.hardware.SensorAdditionalInfo
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.main.R
import com.trainingapp.model.data.Training
import com.trainingapp.view.fragments.HistoryFragment
import kotlin.math.floor

class RunListAdapter(private val context: HistoryFragment) : ListAdapter<Training, RunListAdapter.RunViewHolder>(RunsComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunViewHolder {
        return RunViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RunViewHolder, position: Int) {
        val current = getItem(position)

        val start = context.getString(R.string.training_start, current.startDateTime)
        val end = context.getString(R.string.training_end, current.endDateTime)
        val distance = context.getString(R.string.training_distance, current.distance)
        val duration = context.getString(R.string.training_duration, convertMsToTime(current.duration))

        holder.bind(start, end, distance, duration, current.additionalInfo)
    }

    fun convertMsToTime (milliseconds: Long): String {
        var seconds = floor((milliseconds / 1000.0)).toInt();
        var minutes = floor(seconds / 60.0).toInt();
        var hours = floor(minutes / 60.0).toInt();

        seconds %= 60;
        minutes %= 60;
        hours %= 24;


        return "${hours.toString().padStart(2, '0')}:" +
                "${minutes.toString().padStart(2, '0')}:" +
                seconds.toString().padStart(2, '0')
    }

    class RunViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        private val runItemViewStart: TextView = itemView.findViewById(R.id.startDateTime)
        private val runItemViewEnd: TextView = itemView.findViewById(R.id.endDateTime)
        private val runItemViewDuration: TextView = itemView.findViewById(R.id.duration)
        private val runItemViewDistance: TextView = itemView.findViewById(R.id.distance)
        private val runItemViewAdditionalInfo: TextView = itemView.findViewById(R.id.additionalInfo)

        fun bind(start : String, end: String, distance: String, duration: String, additionalInfo: String) {
            runItemViewStart.text = start
            runItemViewEnd.text = end
            runItemViewDistance.text = distance
            runItemViewDuration.text = duration
            runItemViewAdditionalInfo.text = additionalInfo
        }

        companion object {
            fun create(parent: ViewGroup): RunViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.training_item_running, parent, false)
                return RunViewHolder(view)
            }
        }
    }

    class RunsComparator : DiffUtil.ItemCallback<Training>() {
        override fun areItemsTheSame(oldItem: Training, newItem: Training): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Training, newItem: Training): Boolean {
            return oldItem.distance == newItem.distance &&
                    oldItem.duration == newItem.duration &&
                    oldItem.startDateTime == newItem.endDateTime &&
                    oldItem.additionalInfo == newItem.additionalInfo &&
                    oldItem.endDateTime == newItem.endDateTime
        }
    }
}