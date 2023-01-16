package com.trainingapp

import android.app.Application
import com.trainingapp.model.repository.PrefRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class RunApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())

    val perfRepository by lazy { PrefRepository(applicationContext) }
}