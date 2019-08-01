package com.smartelctronics.smartlock.viewmodel.factory

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.smartelctronics.smartlock.domain.ViaBluetoothUseCase
import com.smartelctronics.smartlock.viewmodel.BluetoothViewModel

class BluetoothViewModelFactory(private val fragment: Fragment): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return BluetoothViewModel(ViaBluetoothUseCase(fragment)) as T
    }
}
