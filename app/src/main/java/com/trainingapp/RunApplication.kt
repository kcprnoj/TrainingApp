package com.trainingapp

import android.app.Application
import com.trainingapp.model.repository.PreferencesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class RunApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())

    val perfRepository by lazy { PreferencesRepository(applicationContext) }
}