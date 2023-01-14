package com.trainingapp.viewmodels.utility

import android.content.Context
import android.content.ContextWrapper
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import java.util.*

class LocaleHelper {
    companion object {

        fun getActualLanguage(preferences : SharedPreferences) : String{
               return preferences.getString("Locale.Helper.Selected.Language", Locale.getDefault().language).toString()
        }

        fun changeLang(context: Context, language: String) : ContextWrapper{
            val resources : Resources = context.resources
            val config : Configuration = resources.configuration

            if(language != ""){
                val locale : Locale = Locale(language)
                Locale.setDefault(locale)
                config.setLocale(locale)
                context.createConfigurationContext(config)
            }
            return ContextWrapper(context)
        }

        fun setLang( editor : SharedPreferences.Editor, language: String){
            editor.putString("Locale.Helper.Selected.Language", language)
            editor.apply()
        }
    }
}