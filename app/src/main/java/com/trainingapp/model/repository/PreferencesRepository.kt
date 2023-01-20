package com.trainingapp.model.repository

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.trainingapp.model.data.UserView

class PreferencesRepository(context: Context) {

    private val pref: SharedPreferences = context.getSharedPreferences(
        PREFERENCE_NAME,
        Context.MODE_PRIVATE)

    private val editor = pref.edit()

    private val gson = Gson()

    fun saveAuthorizationKey(key: String) {
        editor.putString(AUTHORIZATION_KEY, "Bearer $key")
        editor.apply()
    }

    fun getAuthorizationKey():String {
        return pref.getString(AUTHORIZATION_KEY, "")?:""
    }

    fun saveUsername(login: String) {
        editor.putString(USERNAME, login)
        editor.apply()
    }

    fun getUsername():String {
        return pref.getString(USERNAME, "")?:""
    }

    fun saveUser(user: UserView) {
        editor.putString(USER, gson.toJson(user))
        editor.apply()
    }

    fun getUser(): UserView {
        return gson.fromJson(pref.getString(USER, "{}")?:"{}", UserView::class.java)
    }

}