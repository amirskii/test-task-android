package com.example.umangburman.databindingwithlivedata.ViewModel

import android.annotation.SuppressLint
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log

import com.example.umangburman.databindingwithlivedata.Model.LoginUser
import com.example.umangburman.databindingwithlivedata.api.BlockchainService
import com.example.umangburman.databindingwithlivedata.api.Subscribe
import com.example.umangburman.databindingwithlivedata.repository.MyRepository
import com.tinder.scarlet.WebSocket
import javax.inject.Inject

class LoginViewModel @Inject
constructor(private val repository: MyRepository, private val blockchainService: BlockchainService): ViewModel() {

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

    fun getUserInfo() {
        emailAddress.value = repository.getUserInfo()
        setUser()
    }

    fun logout() {
        repository.deleteToken()
    }

    @SuppressLint("CheckResult")
    fun startListen() {
        blockchainService.observeWebSocketEvent()
                .filter { it is WebSocket.Event.OnConnectionOpened<*> }
                .subscribe({
                    blockchainService.sendSubscribe(Subscribe())
                })
        blockchainService.observeTicker()
                .subscribe({ ticker ->
                    Log.d("111","Transaction amount is ${ticker.x.total } at ${ticker.x.datetime}")
                })

    }

}
