package com.smartelctronics.smartlock.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.smartelctronics.smartlock.domain.ViaInternetUseCase
import com.smartelctronics.smartlock.utils.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class InternetViewModel(private val viaInternetUseCase: ViaInternetUseCase) : ViewModel() {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val openState: MutableLiveData<Boolean> = MutableLiveData()
    val shouldRetry: MutableLiveData<Boolean> = MutableLiveData()

    fun bound() {
        compositeDisposable.add(viaInternetUseCase.getLockState()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ state ->
                loading.value = true
                openState.value = state
                Log.i(Constants.TAG, "Result value: $state")
            }, { error ->
                loading.value = true
                shouldRetry.value = true
                Log.i(Constants.TAG, "Error: ${error.message}")
            })
        )
    }

    fun setOpenState(isOpen: Boolean) {
        loading.value = false
        compositeDisposable.add(viaInternetUseCase.setCommand(isOpen)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ state ->
                loading.value = true
                openState.value = state
                Log.i(Constants.TAG, "Result value: $state")
            }, { error ->
                loading.value = true
                Log.i(Constants.TAG, "Error: ${error.message}")
            })
        )
    }

    fun retry() {
        loading.value = false
        shouldRetry.value = false
        bound()
    }

    fun unBound() {
        compositeDisposable.dispose()
        viaInternetUseCase.destroy()
    }
}