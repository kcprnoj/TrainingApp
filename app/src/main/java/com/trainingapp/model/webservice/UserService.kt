package com.trainingapp.model.webservice

import android.util.Log
import com.google.gson.Gson
import com.trainingapp.model.data.UserLogin
import com.trainingapp.model.data.UserRegister
import com.trainingapp.model.data.UserUpdate
import com.trainingapp.model.data.UserView
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
        Log.d("Login", request.toString())
        client.newCall(request).execute().use {
                response -> return response }
    }

    fun getUser(username: String, authorization: String): UserView {
        val request = Request.Builder()
            .url("$url/$username")
            .header("Authorization", authorization)
            .build()
        client.newCall(request).execute().use {
                response -> return gson.fromJson(response.body()?.string() ?: "[]", UserView::class.java)
        }
    }

    fun registerUser(userRegister: UserRegister) : Response{
        val request = Request.Builder()
            .url("$url/register")
            .post(RequestBody.create(JSON, gson.toJson(userRegister)))
            .build()
        Log.d("Register", request.toString())
        client.newCall(request).execute().use { response -> return response }
    }

    fun updateUser(username: String, authorization: String, userUpdate: UserUpdate) : Response{
        val request = Request.Builder()
            .url("$url/$username")
            .put(RequestBody.create(JSON, gson.toJson(userUpdate)))
            .header("Authorization", authorization)
            .build()

        Log.d("Modify", request.toString())
        client.newCall(request).execute().use {
                response -> return response
        }
    }

    fun deleteUser(username: String, authorization: String): Response{
        val request = Request.Builder()
            .url("$url/$username")
            .delete()
            .header("Authorization", authorization)
            .build()

        Log.d("Delete", request.toString())
        client.newCall(request).execute().use {
                response -> return response
        }
    }
}