package com.trainingapp.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.main.R
import com.example.main.databinding.FragmentModifyAccountBinding
import com.trainingapp.ui.MainActivity
import org.json.JSONObject

class ModifyAccountFragment : Fragment() {
    private lateinit var binding: FragmentModifyAccountBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
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

        binding.registerButton.setOnClickListener{
                val newUsername = binding.usernameTextInputLayout.editText?.text.toString()
                val newPassword = binding.newPasswordTextInputLayout.editText?.text.toString()
                val newWeight = binding.weightTextInputLayout.editText?.text.toString()
                val newHeight = binding.heightTextInputLayout.editText?.text.toString()
                var newSex = binding.sexTextInputLayout.editText?.text.toString()
                val password = binding.oldPasswordTextInputLayout.editText?.text.toString()
                val username = (activity as MainActivity).user.login

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

                (activity as MainActivity).stompClient.send("/app/modify",  jsonObject.toString()).subscribe({ }, {
                    Log.d("Login", "Server Error")
                })
        }
    }

    private fun setClientObservers() {
        (activity as MainActivity).modifySuccess.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                findNavController().navigate(ModifyAccountFragmentDirections.actionModifyAccountFragmentToTrainingFragment())
                (activity as MainActivity).modifySuccess.postValue(false)
            }
        })
    }

}