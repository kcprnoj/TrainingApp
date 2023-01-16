package com.trainingapp.view

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.StrictMode
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.main.R
import com.example.main.databinding.ActivityMainBinding
import com.trainingapp.viewmodels.utility.LocaleHelper

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    val registerSuccess = MutableLiveData(false)
    val modifySuccess = MutableLiveData(false)
    val deleteSuccess = MutableLiveData(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setupNavigation()
        setupNightLight()
        setupLanguageChange()
        createNotificationChannel()
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
    }

    private fun setupNavigation(){
        val navController = this.findNavController(R.id.myNavHostFragment)
        val drawerLayout = binding.drawerLayout
        val bottomNavView = binding.bottomNavigation
        val openDrawerButton = binding.openDrawerButton
        bottomNavView.visibility = View.GONE
        val topNavigation = binding.topNavigation

        navController.addOnDestinationChangedListener{ _, destination, _ ->
            when(destination.id){
                R.id.trainingFragment, R.id.historyFragment -> {
                    bottomNavView.visibility = View.VISIBLE
                    openDrawerButton.visibility = View.VISIBLE
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                    navController.graph.startDestination = R.id.trainingFragment
                }
                else -> {
                    bottomNavView.visibility = View.GONE
                    binding.openDrawerButton.visibility = View.GONE
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                }
            }
        }
        openDrawerButton.setOnClickListener {
            if (!drawerLayout.isDrawerOpen(GravityCompat.START)) drawerLayout.openDrawer(GravityCompat.START)
            else drawerLayout.closeDrawer(GravityCompat.END)
        }

        NavigationUI.setupWithNavController(bottomNavView, navController)
        NavigationUI.setupWithNavController(topNavigation, navController)
    }

    private fun setupNightLight(){
        val appSettingPrefs: SharedPreferences = getSharedPreferences("AppSettingPrefs", 0)
        val sharedPrefEdit : SharedPreferences.Editor = appSettingPrefs.edit()
        val isNightModeOn : Boolean = appSettingPrefs.getBoolean("NightMode", false)
        if (isNightModeOn){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            binding.switchNightLight.setIconResource(R.drawable.baseline_wb_sunny_24)
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            binding.switchNightLight.setIconResource(R.drawable.baseline_bedtime_24)
        }

        binding.switchNightLight.setOnClickListener{
            if (isNightModeOn){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                sharedPrefEdit.putBoolean("NightMode", false)
                sharedPrefEdit.apply()
                binding.switchNightLight.setIconResource(R.drawable.baseline_bedtime_24)
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                sharedPrefEdit.putBoolean("NightMode", true)
                sharedPrefEdit.apply()
                binding.switchNightLight.setIconResource(R.drawable.baseline_wb_sunny_24)
            }
        }
    }
    
    private fun setupLanguageChange(){
        val appSettingPrefs: SharedPreferences = getSharedPreferences("AppSettingPrefs", 0)
        val sharedPrefEdit : SharedPreferences.Editor = appSettingPrefs.edit()
        binding.switchPlEng.setOnClickListener {
            if (LocaleHelper.getActualLanguage(appSettingPrefs) == "pl"){
                LocaleHelper.setLang(sharedPrefEdit, "en")
            }else{
                LocaleHelper.setLang(sharedPrefEdit, "pl")
            }
            this.recreate()
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        val appSettingPrefs: SharedPreferences = newBase!!.getSharedPreferences("AppSettingPrefs", 0)
        val lang = LocaleHelper.getActualLanguage(appSettingPrefs)
        val context = LocaleHelper.changeLang(newBase, lang)
        super.attachBaseContext(context)
    }

    private val CHANNEL_ID = "0"

    private fun createNotificationChannel() {
        val name = "Alarm"
        val descriptionText = "Notification of running app"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        // Register the channel with the system
        val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    fun saveAuthorizationKey(key: String) {
        val appSettingPrefs: SharedPreferences = getSharedPreferences("AppSettingPrefs", 0)
        val sharedPrefEdit : SharedPreferences.Editor = appSettingPrefs.edit()
        sharedPrefEdit.putString("Authorization", "Bearer $key")
        sharedPrefEdit.apply()
    }

    fun getAuthorizationKey():String? {
        val appSettingPrefs: SharedPreferences = getSharedPreferences("AppSettingPrefs", 0)
        return appSettingPrefs.getString("Authorization", null)
    }

    fun saveUsername(login: String) {
        val appSettingPrefs: SharedPreferences = getSharedPreferences("AppSettingPrefs", 0)
        val sharedPrefEdit : SharedPreferences.Editor = appSettingPrefs.edit()
        sharedPrefEdit.putString("username", login)
        sharedPrefEdit.apply()
    }

    fun getUsername():String? {
        val appSettingPrefs: SharedPreferences = getSharedPreferences("AppSettingPrefs", 0)
        return appSettingPrefs.getString("username", null)
    }

}