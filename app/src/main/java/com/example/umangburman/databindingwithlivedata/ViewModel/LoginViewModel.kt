package com.example.umangburman.databindingwithlivedata.ViewModel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.view.View

import com.example.umangburman.databindingwithlivedata.Model.LoginUser

class LoginViewModel : ViewModel() {

    var EmailAddress = MutableLiveData<String>()
    var Password = MutableLiveData<String>()

    val userLiveData: MutableLiveData<LoginUser> = MutableLiveData()

    fun onClick(view: View) {

        val loginUser = LoginUser(EmailAddress.value, Password.value)

        userLiveData.value = loginUser

    }

}
