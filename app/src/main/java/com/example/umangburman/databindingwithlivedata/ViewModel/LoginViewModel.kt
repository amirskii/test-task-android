package com.example.umangburman.databindingwithlivedata.ViewModel

import android.annotation.SuppressLint
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log

import com.example.umangburman.databindingwithlivedata.Model.LoginUser
import com.example.umangburman.databindingwithlivedata.Model.Transaction
import com.example.umangburman.databindingwithlivedata.api.BlockchainService
import com.example.umangburman.databindingwithlivedata.api.Subscribe
import com.example.umangburman.databindingwithlivedata.api.Unconfirmed
import com.example.umangburman.databindingwithlivedata.repository.MyRepository
import com.tinder.scarlet.WebSocket
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.Flowables
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class LoginViewModel @Inject
constructor(private val repository: MyRepository, private val blockchainService: BlockchainService): ViewModel() {

    var emailAddress = MutableLiveData<String>()
    var password = MutableLiveData<String>()
    private val compositeDisposable = CompositeDisposable()
    val transactionData = MutableLiveData<Transaction>()
    val sumData = MutableLiveData<Long>().apply { value = 0L }
    var sum = 0L
    private val UPDATE_INTERVAL_SECONDS = 1L

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
        val observable = blockchainService.observeTicker()

        compositeDisposable.addAll(Flowables.combineLatest(
                Flowable.interval(UPDATE_INTERVAL_SECONDS, TimeUnit.SECONDS),
                observable
        ).map { (_, transactionBook) -> transactionBook }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    showTransaction(it)
                }, {
                    Log.e("Error", it.message)
                }))

    }

    fun showTransaction(unconfirmed: Unconfirmed) {
        sum += unconfirmed.x.total
        sumData.postValue(sum)
        transactionData.postValue(Transaction(unconfirmed.x.datetime.toString(), unconfirmed.x.total))
        Log.d("111","Transaction amount is ${unconfirmed.x.total } at ${unconfirmed.x.datetime}")
    }

    fun stopListening() {
        compositeDisposable.clear()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}
