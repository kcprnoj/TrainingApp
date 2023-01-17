package com.trainingapp.model.repository

import com.trainingapp.model.data.UserLogin
import com.trainingapp.model.data.UserRegister
import com.trainingapp.model.data.UserUpdate
import com.trainingapp.model.webservice.UserService
import okhttp3.Response

class UserRepository (private val userService: UserService){

    fun login(user: UserLogin) : Response{
        return userService.login(user)
    }

    fun registerUser(user: UserRegister) : Response{
        return userService.registerUser(user)
    }

    fun updateUser(username: String, authorization: String, userUpdate: UserUpdate) :Response{
        return userService.updateUser(username, authorization, userUpdate)
    }

    fun deleteUser(username: String, authorization: String) : Response{
        return userService.deleteUser(username,authorization)
    }
}