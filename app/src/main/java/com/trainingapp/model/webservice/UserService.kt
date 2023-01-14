package com.trainingapp.model.webservice

import com.google.gson.Gson
import com.trainingapp.model.data.UserLogin
import okhttp3.*

class UserService {
    private val client = OkHttpClient()
    private val url = "http://10.0.2.2:8080/api/user"
    private val gson = Gson()

    private val JSON: MediaType = MediaType.get("application/json; charset=utf-8")


    fun login(user: UserLogin): Response {
        val request = Request.Builder()
            .url("$url/login")
            .post(RequestBody.create(JSON, gson.toJson(user)))
            .build()

        client.newCall(request).execute().use { response -> return response }
    }
}