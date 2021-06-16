package com.trainingapp.viewmodels

import androidx.lifecycle.ViewModel

class CreateReminderViewModel : ViewModel(){

    fun formatDay(dayOfMonth: Int): String{
        return if (dayOfMonth >= 10)
            dayOfMonth.toString()
        else
            "0$dayOfMonth"
    }

    fun formatMonth(month: Int): String{
        return if (month+1 >= 10)
            (month+1).toString()
        else
            "0${month+1}"
    }
}