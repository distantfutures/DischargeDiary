package com.example.dischargediary.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(): ViewModel(){
    private var _navigateToDiary = MutableLiveData<Boolean>()
            val navigateToDiary: LiveData<Boolean> = _navigateToDiary
    init {
        _navigateToDiary.value = true
    }
    fun loginValid() {
        _navigateToDiary.value = true
    }
    fun doneNavigating(){
        _navigateToDiary.value = false
    }
}