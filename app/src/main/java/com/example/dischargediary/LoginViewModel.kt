package com.example.dischargediary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel: ViewModel(){

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