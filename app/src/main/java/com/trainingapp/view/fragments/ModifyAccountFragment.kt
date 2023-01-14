package com.trainingapp.view.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.main.R
import com.example.main.databinding.FragmentModifyAccountBinding
import com.trainingapp.view.MainActivity
import org.json.JSONObject

class ModifyAccountFragment : Fragment() {
    private lateinit var binding: FragmentModifyAccountBinding
    private val CHANNEL_ID = "0"
    private var skipDelete = true
    private var skipModify = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_modify_account, container, false)
        binding.lifecycleOwner = this

        setClientObservers()
        setListeners()

        val items = listOf(resources.getString(R.string.male), resources.getString(R.string.female), resources.getString(R.string.other))
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
        binding.sexAuto.setAdapter(adapter)

        return binding.root
    }

    private fun setListeners(){
        binding.backButton.setOnClickListener{
            findNavController().navigate(ModifyAccountFragmentDirections.actionModifyAccountFragmentToTrainingFragment())
        }

        binding.deleteAccountButton.setOnClickListener{
            val appSettingPrefs: SharedPreferences = (activity as MainActivity).getSharedPreferences("AppSettingPrefs",0)
            val username = appSettingPrefs.getString("userLogin", "Kacper")
            val password = binding.oldPasswordTextInputLayout.editText?.text.toString()

            val jsonObject = JSONObject()
            jsonObject.put("login", username)
            jsonObject.put("password", password)

            /* TODO: Zastapic uzywajac api
            (activity as MainActivity).stompClient.send("/app/delete",  jsonObject.toString()).subscribe({ }, {
                Log.d("Login", "Server Error")
            })*/
        }

        binding.registerButton.setOnClickListener{
            val appSettingPrefs: SharedPreferences = (activity as MainActivity).getSharedPreferences("AppSettingPrefs",0)
            val newUsername = binding.usernameTextInputLayout.editText?.text.toString()
            val newPassword = binding.newPasswordTextInputLayout.editText?.text.toString()
            val newWeight = binding.weightTextInputLayout.editText?.text.toString()
            val newHeight = binding.heightTextInputLayout.editText?.text.toString()
            var newSex = binding.sexTextInputLayout.editText?.text.toString()
            val password = binding.oldPasswordTextInputLayout.editText?.text.toString()
            val username = appSettingPrefs.getString("userLogin", "Kacper")

            newSex = when(newSex) {
                resources.getString(R.string.male) -> "male"
                resources.getString(R.string.female) -> "female"
                resources.getString(R.string.other) -> "other"
                else -> ""
            }

            val jsonObject = JSONObject()
            jsonObject.put("new login", newUsername)
            jsonObject.put("new password", newPassword)
            jsonObject.put("new weight", newWeight)
            jsonObject.put("new height", newHeight)
            jsonObject.put("new sex", newSex)
            jsonObject.put("login", username)
            jsonObject.put("password", password)

            /* TODO: Zastapic uzywajac repo
            (activity as MainActivity).stompClient.send("/app/modify",  jsonObject.toString()).subscribe({ }, {
                Log.d("Login", "Server Error")
            })*/
        }
    }

    private fun setClientObservers() {
        (activity as MainActivity).modifySuccess.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                findNavController().navigate(ModifyAccountFragmentDirections.actionModifyAccountFragmentToTrainingFragment())
                skipModify = true
                (activity as MainActivity).modifySuccess.postValue(false)
            } else {
                if (!skipModify)
                    Toast.makeText(requireActivity(), getString(R.string.failed_modify), Toast.LENGTH_SHORT).show()
                else
                    skipModify = false
            }
        })

        (activity as MainActivity).deleteSuccess.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                findNavController().navigate(ModifyAccountFragmentDirections.actionModifyAccountFragmentToLoginFragment())
                skipDelete = true
                notifyDelete()
                (activity as MainActivity).deleteSuccess.postValue(false)
            } else {
                if (!skipDelete)
                    Toast.makeText(requireActivity(), getString(R.string.failed_delete), Toast.LENGTH_SHORT).show()
                else
                    skipDelete = false
            }
        })
    }

    private fun notifyDelete() {
        Log.d("notify", "delete")

        val builder = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_directions_run_24)
                .setContentTitle(getString(R.string.success))
                .setContentText(getString(R.string.notification_delete))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(requireContext())) {
            notify(0, builder.build())
        }
    }

}