package com.smartelctronics.smartlock.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.smartelctronics.smartlock.domain.ViaBluetoothUseCase
import com.smartelctronics.smartlock.utils.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class BluetoothViewModel(private val viaBluetoothUseCase: ViaBluetoothUseCase) : ViewModel() {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val openState: MutableLiveData<Boolean> = MutableLiveData()
    val shouldRetry: MutableLiveData<Boolean> = MutableLiveData()
    val connectionState: MutableLiveData<Boolean> = MutableLiveData()

    fun bound() = viaBluetoothUseCase.initBluetooth()

    fun permissionGranted() = viaBluetoothUseCase.enableBluetooth()

    // When Location disable app kill. Its bug
    fun bluetoothEnable() {
        Log.i(Constants.TAG, "BLE Enable")
        viaBluetoothUseCase.scan()
            .flatMap { viaBluetoothUseCase.connect(it) }
            .flatMap {
                Log.i(Constants.TAG, "Connected")
                connectionState.postValue(true)
                viaBluetoothUseCase.getLockState()
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ state ->
                loading.postValue(true)
                openState.value = state
                Log.i(Constants.TAG, "Open state: $state")
            }, {
                Log.e(Constants.TAG, it.message)
            })
            .let { compositeDisposable.add(it) }
    }

    fun setOpenState(isOpen: Boolean) {
        Log.i(Constants.TAG, "$isOpen")
        loading.value = false
        viaBluetoothUseCase.setCommand(isOpen)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ state ->
                loading.value = true
                openState.value = state
                Log.i(Constants.TAG, "Open state: $state")
            }, { error ->
                loading.value = true
                Log.i(Constants.TAG, "Error: ${error.message}")
            })
            .let { compositeDisposable.add(it) }
    }

    fun retry() {
        loading.value = false
        shouldRetry.value = false
        bound()
    }

    fun unBound() {
        compositeDisposable.dispose()
        viaBluetoothUseCase.destroy()
    }
}