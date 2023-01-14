package com.trainingapp.model.data

import java.sql.Timestamp

data class TrainingCreate(
    val startDateTime: Long,
    val endDateTime: Long,
    val distance: Double,
    val duration: Long,
    val additionalInfo: String,
    val username: String
)