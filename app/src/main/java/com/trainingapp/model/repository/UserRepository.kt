package com.trainingapp.model.repository

import com.trainingapp.model.data.UserLogin
import com.trainingapp.model.data.UserRegister
import com.trainingapp.model.data.UserUpdate
import com.trainingapp.model.data.UserView
import com.trainingapp.model.webservice.UserService
import okhttp3.Response

class UserRepository (private val userService: UserService, private val pref: PrefRepository){

    fun login(user: UserLogin) : String?{
        val response = userService.login(user)
        return if (response.isSuccessful)
            response.headers("Authorization")[0].toString()
        else
            null
    }

    fun getUser(username: String): UserView {
        return userService.getUser(username, pref.getAuthorizationKey())
    }

    fun registerUser(user: UserRegister) : Response{
        return userService.registerUser(user)
    }

    fun updateUser(username: String, userUpdate: UserUpdate): Response{
        return userService.updateUser(username, pref.getAuthorizationKey(), userUpdate)
    }

    fun deleteUser(username: String): Response{
        return userService.deleteUser(username, pref.getAuthorizationKey())
    }
}