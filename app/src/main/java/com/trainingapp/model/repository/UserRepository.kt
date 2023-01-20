package com.trainingapp.model.repository

import com.trainingapp.model.data.UserLogin
import com.trainingapp.model.data.UserRegister
import com.trainingapp.model.data.UserUpdate
import com.trainingapp.model.data.UserView
import com.trainingapp.model.webservice.UserService

class UserRepository (private val userService: UserService){

    fun login(user: UserLogin) : String?{
        val response = userService.login(user)
        return if (response.isSuccessful)
            response.headers("Authorization")[0].toString()
        else
            null
    }

    fun getUser(username: String, authorizationKey: String): UserView {
        return userService.getUser(username, authorizationKey)
    }

    fun registerUser(user: UserRegister) : Boolean{
        val response = userService.registerUser(user)
        return response.isSuccessful
    }

    fun updateUser(username: String, authorizationKey: String, userUpdate: UserUpdate): Boolean{
        val response = userService.updateUser(username, authorizationKey, userUpdate)
        return response.isSuccessful
    }

    fun deleteUser(username: String, authorizationKey: String): Boolean{
        val response = userService.deleteUser(username, authorizationKey)
        return response.isSuccessful
    }
}