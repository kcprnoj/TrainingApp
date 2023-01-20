package com.trainingapp.model.data

data class TrainingCreate(
    val startDateTime: Long,
    val endDateTime: Long,
    val distance: Double,
    val duration: Long,
    val additionalInfo: String,
    var username: String
)