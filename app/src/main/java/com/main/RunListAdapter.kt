package com.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.main.R
import com.main.database.Run

class RunListAdapter : ListAdapter<Run, RunListAdapter.RunViewHolder>(RunsComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunViewHolder {
        return RunViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RunViewHolder, position: Int) {
        var current = getItem(position)
        holder.bind(current.id.toString() + " " + current.distance.toString() + " km"
                    + current.calories.toString() + " calories " + current.avgSpeed.toString() + " avg ")
    }

    class RunViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        private val runItemView: TextView = itemView.findViewById(R.id.textView)

        fun bind(text: String?) {
            runItemView.text = text
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