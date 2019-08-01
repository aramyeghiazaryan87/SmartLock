package com.smartelctronics.smartlock.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.smartelctronics.smartlock.utils.ConnectionType

class MainViewModel: ViewModel() {

    var connectionType: MutableLiveData<ConnectionType> = MutableLiveData()

    fun onClick(type: ConnectionType) {
        connectionType.value = type
    }

    fun unBound() {

    }

}