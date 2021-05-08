package com.main.database

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "run_table")
data class Run(
        var timestamp: Long = 0L,
        var avgSpeed: Float = 0f,
        var distance: Float = 0f,
        var time: Long = 0L,
        var calories: Int = 0
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}