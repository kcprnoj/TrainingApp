package com.trainingapp.model.data

import java.time.LocalDate

data class UserRegister (
    var username: String,
    var password: String,
    var sex: String,
    var weight: Double,
    var height: Double,
    var birthday: LocalDate
)