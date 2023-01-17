package com.trainingapp.model.data

import java.time.LocalDate

data class UserUpdate(
    var sex: String,
    var weight: Double,
    var height: Double,
    var birthday: LocalDate
)