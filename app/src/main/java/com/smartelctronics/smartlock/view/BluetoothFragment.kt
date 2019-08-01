package com.smartelctronics.smartlock.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.smartelctronics.smartlock.databinding.FragmentBluetoothBinding
import com.smartelctronics.smartlock.utils.LockToggle
import com.smartelctronics.smartlock.viewmodel.BluetoothViewModel
import com.smartelctronics.smartlock.viewmodel.factory.BluetoothViewModelFactory
import android.content.Intent
import android.content.pm.PackageManager
import android.app.Activity
import androidx.annotation.NonNull
import com.smartelctronics.smartlock.R
import com.smartelctronics.smartlock.utils.Constants


class BluetoothFragment : Fragment() {

    private lateinit var bluetoothViewModel: BluetoothViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        val binding: FragmentBluetoothBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_bluetooth, container, false)
        bluetoothViewModel = ViewModelProviders.of(this, BluetoothViewModelFactory(this))
            .get(BluetoothViewModel::class.java)
        binding.viewModel = bluetoothViewModel
        binding.lifecycleOwner = this
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<LockToggle>(R.id.lock_toggle).onToggleListener = object : LockToggle.OnToggleListener {
            override fun onToggle(openState: Boolean) {
                bluetoothViewModel.setOpenState(openState)
            }
        }
        bluetoothViewModel.bound()
    }

    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        when (requestCode) {
            Constants.PERMISSION_REQUEST_COARSE_LOCATION -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    bluetoothViewModel.permissionGranted()
                }
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                bluetoothViewModel.bluetoothEnable()
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        bluetoothViewModel.unBound()
    }
}