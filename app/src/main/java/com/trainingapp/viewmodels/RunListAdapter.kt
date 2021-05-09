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
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class RunListAdapter : ListAdapter<Run, RunListAdapter.RunViewHolder>(RunsComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunViewHolder {
        return RunViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RunViewHolder, position: Int) {
        val current = getItem(position)

        val currentDate = Instant.ofEpochSecond(current.timestamp)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")


        holder.bind(current.id.toString() + ". " + currentDate.format(formatter),
                current.distance.toString() + " km \n" + current.calories.toString() + " cal "
                        + current.avgSpeed.toString() + " avg ")
    }

    class RunViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        private val runItemViewTitle: TextView = itemView.findViewById(R.id.title)
        private val runItemViewDes: TextView = itemView.findViewById(R.id.desc)

        fun bind(title: String?, text: String?) {
            runItemViewTitle.text = title
            runItemViewDes.text = text
        }

        companion object {
            fun create(parent: ViewGroup): RunViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.training_item, parent, false)
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