package com.smartelctronics.smartlock.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.smartelctronics.smartlock.domain.ViaInternetUseCase
import com.smartelctronics.smartlock.viewmodel.InternetViewModel

class InternetViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return InternetViewModel(ViaInternetUseCase()) as T
    }
}