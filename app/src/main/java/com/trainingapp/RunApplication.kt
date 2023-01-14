package com.trainingapp

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class RunApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())
}