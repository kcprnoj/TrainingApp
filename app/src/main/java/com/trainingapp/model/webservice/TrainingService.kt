package com.trainingapp.model.webservice

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.trainingapp.model.data.Training
import com.trainingapp.model.data.TrainingCreate
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

class TrainingService {
    private val client = OkHttpClient()
    private val url = "http://10.0.2.2:8080/api/training"
    private val gson = Gson()

    private val JSON: MediaType = MediaType.get("application/json; charset=utf-8")


    fun getTrainings(username: String, authorization: String): List<Training> {
        val request = Request.Builder()
            .url("$url/user/$username")
            .header("Authorization", authorization)
            .build()

        val typeToken = object : TypeToken<List<Training>>() {}.type

        client.newCall(request).execute().use {
            response -> return gson.fromJson(response.body()?.string() ?: "[]", typeToken)
        }
    }

    fun addTraining(training: TrainingCreate, authorization: String) {
        val request = Request.Builder()
            .url(url)
            .header("Authorization", authorization)
            .post(RequestBody.create(JSON, gson.toJson(training)))
            .build()

        println(request)

        println(gson.toJson(training))

        println(training.toString())

        client.newCall(request).execute().use { response -> println(response) }
    }
}