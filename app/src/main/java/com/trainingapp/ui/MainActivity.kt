package com.trainingapp.ui

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.main.R
import com.example.main.databinding.ActivityMainBinding
import com.trainingapp.utility.LocaleHelper
import org.json.JSONObject
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var stompClient: StompClient


    val loginSuccess = MutableLiveData<Boolean>(false)
    val registerSuccess = MutableLiveData<Boolean>(false)
    val modifySuccess = MutableLiveData<Boolean>(false)
    val deleteSuccess = MutableLiveData<Boolean>(false)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setupNavigation()
        connectToServer()
        setupNightLight()
        setupLanguageChange()
        createNotificationChannel()
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
                R.id.trainingFragment, R.id.historyFragment, R.id.statisticsFragment -> {
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


    @SuppressLint("CheckResult")
    private fun connectToServer(): Boolean {
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://10.0.2.2:8080/chat")
        stompClient.connect()
        stompClient.lifecycle().subscribe{
            if (it.type == LifecycleEvent.Type.CLOSED) {
                connectToServer()
            }
        }

        subscribeTopicModify()
        subscribeTopicLogin()
        subscribeTopicRegister()
        subscribeTopicDelete()
        subscribeTopicAdmin()

        Log.i("Server", "Connected")
        return true
    }

    @SuppressLint("CheckResult")
    private fun subscribeTopicRegister() {
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
        }, {
            Log.d("Server", "Topic failed")
            if (stompClient.isConnected) {
                subscribeTopicRegister()
            }
        })
    }

    @SuppressLint("CheckResult")
    private fun subscribeTopicLogin() {
        stompClient.topic("/user/queue/login").subscribe({ topicMessage ->
            val reply = JSONObject(topicMessage.payload)
            when {
                reply.getString("Successful") == "True" -> {

                    val appSettingPrefs: SharedPreferences = getSharedPreferences("AppSettingPrefs", 0)
                    val sharedPrefEdit: SharedPreferences.Editor = appSettingPrefs.edit()
                    sharedPrefEdit.putString("userLogin", reply.getString("login"))
                    sharedPrefEdit.putFloat("userWeight", reply.getString("weight").toFloat())
                    sharedPrefEdit.putFloat("userHeight", reply.getString("height").toFloat())
                    sharedPrefEdit.putString("userSex", reply.getString("sex"))
                    sharedPrefEdit.apply()

                    loginSuccess.postValue(true)
                }
                else -> {
                    Log.d("Login", "Failed")
                    loginSuccess.postValue(false)
                }
            }
        }, {
            Log.d("Server", "Topic failed")

            if (stompClient.isConnected) {
                subscribeTopicLogin()
            }
        })
    }

    @SuppressLint("CheckResult")
    private fun subscribeTopicModify() {
        stompClient.topic("/user/queue/modify").subscribe({ topicMessage ->
            val reply = JSONObject(topicMessage.payload)
            when {
                reply.getString("Successful") == "True" -> {

                    val appSettingPrefs: SharedPreferences = getSharedPreferences("AppSettingPrefs", 0)
                    val sharedPrefEdit: SharedPreferences.Editor = appSettingPrefs.edit()
                    sharedPrefEdit.putString("userLogin", reply.getString("login"))
                    sharedPrefEdit.putFloat("userWeight", reply.getString("weight").toFloat())
                    sharedPrefEdit.putFloat("userHeight", reply.getString("height").toFloat())
                    sharedPrefEdit.putString("userSex", reply.getString("sex"))
                    sharedPrefEdit.apply()

                    modifySuccess.postValue(true)
                }
                else -> {
                    Log.d("Modify", "Failed")
                    modifySuccess.postValue(false)
                }
            }
        }, {
            Log.d("Server", "Topic failed")
            if (stompClient.isConnected) {
                subscribeTopicModify()
            }
        })
    }

    @SuppressLint("CheckResult")
    private fun subscribeTopicDelete() {
        stompClient.topic("/user/queue/delete").subscribe({ topicMessage ->
            val reply = JSONObject(topicMessage.payload)
            when {
                reply.getString("Successful") == "True" -> {

                    deleteSuccess.postValue(true)
                }
                else -> {
                    Log.d("Login", "Failed")
                    deleteSuccess.postValue(false)
                }
            }
        }, {
            Log.d("Server", "Topic failed")

            if (stompClient.isConnected) {
                subscribeTopicLogin()
            }
        })
    }

    @SuppressLint("CheckResult")
    private fun subscribeTopicAdmin() {
        stompClient.topic("/topic/admin").subscribe({ topicMessage ->
            val reply = topicMessage.payload
            if (reply != null && reply != "") {
                notifyMessage(reply)
            } else {
                Log.d("Server", "No msg")
            }
        }, {
            Log.d("Server", "Topic failed")

            if (stompClient.isConnected) {
                subscribeTopicLogin()
            }
        })
    }

    override fun onDestroy() {
        if (stompClient.isConnected) {
            stompClient.disconnect()
        }
        super.onDestroy()
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

    private fun notifyMessage(message: String) {
        Log.d("notify", "message")

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_directions_run_24)
                .setContentTitle(getString(R.string.admin_mess))
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            notify(0, builder.build())
        }
    }

}