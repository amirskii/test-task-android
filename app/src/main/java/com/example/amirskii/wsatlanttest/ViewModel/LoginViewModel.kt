package com.example.amirskii.wsatlanttest.ViewModel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.amirskii.wsatlanttest.Model.LoginUser
import com.example.amirskii.wsatlanttest.repository.MyRepository
import javax.inject.Inject

class LoginViewModel @Inject
constructor(private val repository: MyRepository): ViewModel() {

    var emailAddress = MutableLiveData<String>()
    var password = MutableLiveData<String>()

    val userLiveData: MutableLiveData<LoginUser> = MutableLiveData()

    fun setUser() {
        val loginUser = LoginUser(emailAddress.value, password.value)
        userLiveData.value = loginUser
    }

    fun login() = repository.login(emailAddress.value ?: "", password.value ?: "")

    fun saveToken(token: String) = repository.saveToken(token, emailAddress.value ?: "")

    fun getSession() = repository.getSession()
}
