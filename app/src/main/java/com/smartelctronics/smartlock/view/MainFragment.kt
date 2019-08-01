package com.smartelctronics.smartlock.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.smartelctronics.smartlock.R
import com.smartelctronics.smartlock.databinding.FragmentMainBinding
import com.smartelctronics.smartlock.utils.ConnectionType
import com.smartelctronics.smartlock.viewmodel.MainViewModel

class MainFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentMainBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        binding.viewModel = mainViewModel
        mainViewModel.connectionType.observe(viewLifecycleOwner, Observer { connectionType ->
            if (findNavController().currentDestination?.id == R.id.mainFragment)
                when (connectionType) {
                    ConnectionType.INTERNET -> findNavController().navigate(
                        MainFragmentDirections.actionMainFragmentToInternetFragment()
                    )

                    ConnectionType.BLUETOOTH -> findNavController().navigate(
                        MainFragmentDirections.actionMainFragmentToBluetoothFragment()
                    )
                }

        })
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        mainViewModel.unBound()
    }

}
