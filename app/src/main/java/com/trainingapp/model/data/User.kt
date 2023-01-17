package com.trainingapp.model.data

import java.time.LocalDate

data class User(
    var username: String,
    var height: Double,
    var weight: Double,
    var sex: String,
    var birthday: LocalDate
)