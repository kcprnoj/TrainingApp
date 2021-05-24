package com.trainingapp.ui

import android.annotation.SuppressLint
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.MutableBoolean
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.main.R
import com.example.main.databinding.ActivityMainBinding
import com.trainingapp.ui.fragments.LoginFragmentDirections
import com.trainingapp.ui.fragments.RegisterFragmentDirections
import com.trainingapp.utility.User
import org.json.JSONObject
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import java.lang.Exception



class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration : AppBarConfiguration
    lateinit var stompClient: StompClient
    val user = User()

    val loginSuccess = MutableLiveData<Boolean>(false)
    val registerSuccess = MutableLiveData<Boolean>(false)
    val modifySuccess = MutableLiveData<Boolean>(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setupNavigation()
        connectToServer()
        setupNightLight()
    }

    private fun setupNavigation(){
        val navController = this.findNavController(R.id.myNavHostFragment)
        val drawerLayout = binding.drawerLayout
        val bottomNavView = binding.bottomNavigation
        bottomNavView.visibility = View.GONE
        val topNavigation = binding.topNavigation
        //NavigationUI.setupActionBarWithNavController(this,navController,drawerLayout)
        //appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)


        navController.addOnDestinationChangedListener{ _, destination, _ ->
            when(destination.id){
                R.id.trainingFragment, R.id.historyFragment, R.id.statisticsFragment -> {
                    bottomNavView.visibility = View.VISIBLE
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                    navController.graph.startDestination = R.id.trainingFragment
                }
                else -> {
                    bottomNavView.visibility = View.GONE
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                }
            }
        }
        NavigationUI.setupWithNavController(bottomNavView, navController)
        NavigationUI.setupWithNavController(topNavigation, navController)
    }
//    override fun onSupportNavigateUp(): Boolean {
//        val navController = this.findNavController(R.id.myNavHostFragment)
//        return NavigationUI.navigateUp(navController, appBarConfiguration)
//    }

    private fun setupNightLight(){
        val appSettingPrefs: SharedPreferences = getSharedPreferences("AppSettingPrefs",0)
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

    }

    @SuppressLint("CheckResult")
    private fun connectToServer(): Boolean {
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://10.0.2.2:8080/chat")
        stompClient.connect()


        stompClient.topic("/user/queue/login").subscribe({ topicMessage ->
            val reply = JSONObject(topicMessage.payload)
            when {
                reply.getString("Successful") == "True" -> {
                    user.login = reply.getString("login")
                    user.weight = reply.getString("weight").toFloat()
                    user.height = reply.getString("height").toFloat()
                    user.sex = reply.getString("sex")

                    loginSuccess.postValue(true)
                }
                else -> {
                    Log.d("Login", "Failed")
                    loginSuccess.postValue(false)
                }
            }
        }, {Log.d("Server", "Topic failed")})

        stompClient.topic("/user/queue/register").subscribe({ topicMessage ->
            val reply = JSONObject(topicMessage.payload)
            when {
                reply.getString("Successful") == "True" -> {
                    registerSuccess.postValue(true)
                }
                else -> {
                    Log.d("Register", "Failed")
                    registerSuccess.postValue(false)
                }
            }
        }, {Log.d("Server", "Topic failed")})

        stompClient.topic("/user/queue/modify").subscribe({ topicMessage ->
            val reply = JSONObject(topicMessage.payload)
            when {
                reply.getString("Successful") == "True" -> {
                    user.login = reply.getString("login")
                    user.weight = reply.getString("weight").toFloat()
                    user.height = reply.getString("height").toFloat()
                    user.sex = reply.getString("sex")
                    modifySuccess.postValue(true)
                }
                else -> {
                    Log.d("Modify", "Failed")
                    modifySuccess.postValue(false)
                }
            }
        }, {Log.d("Server", "Topic failed")})

        Log.i("Connection", "Connected")
        return true
    }

    override fun onDestroy() {
        if (stompClient.isConnected) {
            stompClient.disconnect()
        }
        super.onDestroy()
    }

}