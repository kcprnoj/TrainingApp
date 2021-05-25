package com.trainingapp.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "training_table")
class Training (
    var timestamp: Long = 0L,
    var name: String = "",
    var time: Long = 0L,
    var calories: Int = 0) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}