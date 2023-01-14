package com.trainingapp.model.data

import java.sql.Timestamp

data class Training(
        var startDateTime: Timestamp,
        var endDateTime: Timestamp,
        var distance: Double,
        var duration: Long,
        var additionalInfo: String
)