package com.smartelctronics.smartlock.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.smartelctronics.smartlock.R
import com.smartelctronics.smartlock.databinding.FragmentInternetBinding
import com.smartelctronics.smartlock.domain.ViaInternetUseCase
import com.smartelctronics.smartlock.utils.LockToggle
import com.smartelctronics.smartlock.viewmodel.InternetViewModel
import com.smartelctronics.smartlock.viewmodel.factory.InternetViewModelFactory


class InternetFragment : Fragment() {

    private lateinit var internetViewModel: InternetViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        val binding: FragmentInternetBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_internet, container, false)
        internetViewModel = ViewModelProviders.of(this, InternetViewModelFactory()).get(InternetViewModel::class.java)
        binding.viewModel = internetViewModel
        binding.lifecycleOwner = this
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<LockToggle>(R.id.lock_toggle).onToggleListener = object : LockToggle.OnToggleListener {
            override fun onToggle(openState: Boolean) {
                internetViewModel.setOpenState(openState)
            }
        }
        internetViewModel.bound()
    }

    override fun onDetach() {
        super.onDetach()
        internetViewModel.unBound()
    }
}

