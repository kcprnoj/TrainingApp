package com.trainingapp.viewmodels

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.main.R
import com.trainingapp.db.Run
import com.trainingapp.ui.fragments.HistoryFragment
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class RunListAdapter(private val context: HistoryFragment) : ListAdapter<Run, RunListAdapter.RunViewHolder>(RunsComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunViewHolder {
        return RunViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RunViewHolder, position: Int) {
        val current = getItem(position)

        val currentDate = Instant.ofEpochMilli(current.timestamp)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
        val title = context.getString(R.string.training_title, current.id,
                currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))

        val distance = context.getString(R.string.training_distance, current.distance/1000)

        val time = context.getString(R.string.training_time,
                String.format("%02d:%02d:%02d", current.time/1000/60/60%60,
                        current.time/1000/60%60, current.time/1000%60))

        val avg = context.getString(R.string.training_avg, current.avgSpeed)

        val calories = context.getString(R.string.training_calories, current.calories)

        holder.bind(title, distance, time, avg, calories)
    }

    class RunViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        private val runItemViewTitle: TextView = itemView.findViewById(R.id.title)
        private val runItemViewDistance: TextView = itemView.findViewById(R.id.distance)
        private val runItemViewTime: TextView = itemView.findViewById(R.id.time)
        private val runItemViewAvg: TextView = itemView.findViewById(R.id.avg)
        private val runItemViewCalories: TextView = itemView.findViewById(R.id.calories)

        fun bind(title : String, distance: String, time: String, avg: String, calories: String) {
            runItemViewTitle.text = title
            runItemViewDistance.text = distance
            runItemViewTime.text = time
            runItemViewAvg.text = avg
            runItemViewCalories.text = calories
        }

        companion object {
            fun create(parent: ViewGroup): RunViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.training_item_running, parent, false)
                return RunViewHolder(view)
            }
        }
    }

    class RunsComparator : DiffUtil.ItemCallback<Run>() {
        override fun areItemsTheSame(oldItem: Run, newItem: Run): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Run, newItem: Run): Boolean {
            return oldItem.avgSpeed == newItem.avgSpeed &&
                    oldItem.distance == newItem.distance &&
                    oldItem.timestamp == newItem.timestamp &&
                    oldItem.calories == newItem.calories &&
                    oldItem.time == newItem.time
        }
    }
}