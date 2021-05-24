package com.trainingapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import com.example.main.R
import com.example.main.databinding.FragmentRegisterBinding

class ModifyAccountFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        binding.lifecycleOwner = this

        val items = listOf(resources.getString(R.string.male), resources.getString(R.string.female), resources.getString(R.string.other))
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
        binding.sexAuto.setAdapter(adapter)

        return inflater.inflate(R.layout.fragment_modify_account, container, false)
    }

}