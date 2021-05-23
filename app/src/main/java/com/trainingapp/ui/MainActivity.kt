package com.trainingapp.ui

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.main.R
import com.example.main.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration : AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setupNavigation()
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

}