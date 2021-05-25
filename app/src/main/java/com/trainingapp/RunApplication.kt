package com.trainingapp

import android.app.Application
import com.trainingapp.db.RunDatabase
import com.trainingapp.db.TrainingDatabase
import com.trainingapp.repositories.RunRepository
import com.trainingapp.repositories.TrainingRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class RunApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())

    private val database by lazy { RunDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { RunRepository(database.getRunDao()) }

    private val databaseTraining by lazy { TrainingDatabase.getDatabase(this, applicationScope) }
    val repositoryTraining by lazy { TrainingRepository(databaseTraining.getTrainingDao()) }
}