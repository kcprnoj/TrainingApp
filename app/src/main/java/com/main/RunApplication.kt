package com.main

import android.app.Application
import com.main.database.RunDatabase
import com.main.database.RunRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class RunApplication : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy { RunDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { RunRepository(database.getRunDao()) }
}