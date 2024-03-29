package com.example.amirskii.wsatlanttest.ViewModel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.example.amirskii.wsatlanttest.Model.LoginUser
import com.example.amirskii.wsatlanttest.Model.Transaction
import com.example.amirskii.wsatlanttest.api.Unconfirmed
import com.example.amirskii.wsatlanttest.repository.BlockchainRepository
import com.example.amirskii.wsatlanttest.repository.MyRepository
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.Flowables
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainViewModel @Inject
constructor(private val repository: MyRepository,
            private val blockchainRepository: BlockchainRepository): ViewModel() {

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

    fun getUserInfo() {
        repository.getSession()
        emailAddress.value = repository.getUserInfo()
        setUser()
    }

    fun logout() {
        repository.deleteToken()
    }

    fun startListening() {
        compositeDisposable.addAll(Flowables.combineLatest(
                Flowable.interval(UPDATE_INTERVAL_SECONDS, TimeUnit.SECONDS),
                blockchainRepository.getTransactionObservable()
        ).map { (_, transactionBook) -> transactionBook }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    showTransaction(it)
                }, {
                    Log.e("Error", it.message)
                }))

    }

    fun showTransaction(unconfirmed: Unconfirmed) {
        setSummary(sum + unconfirmed.x.total)
        transactionData.postValue(Transaction(unconfirmed.x.datetime.toString(), unconfirmed.x.total))
        Log.d("111","Transaction amount is ${unconfirmed.x.total } at ${unconfirmed.x.datetime}")
    }

    fun stopListening() {
        compositeDisposable.clear()
    }

    fun setSummary(newSum: Long) {
        sum = newSum
        sumData.postValue(sum)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}
