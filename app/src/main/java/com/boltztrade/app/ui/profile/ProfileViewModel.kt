package com.boltztrade.app.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.boltztrade.app.model.BoltztradeUserDetail

class ProfileViewModel : ViewModel() {

    private val userDetail = MutableLiveData<BoltztradeUserDetail>()

    fun getUserDetail():MutableLiveData<BoltztradeUserDetail>{
        return userDetail
    }
}
