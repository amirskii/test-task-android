package com.example.amirskii.wsatlanttest.repository

import android.util.Log
import com.example.amirskii.wsatlanttest.api.BlockchainService
import com.example.amirskii.wsatlanttest.api.Subscribe
import com.example.amirskii.wsatlanttest.api.Unconfirmed
import com.tinder.scarlet.WebSocket
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class BlockchainRepository @Inject constructor(val blockchainService: BlockchainService) {

    init {
        blockchainService.observeWebSocketEvent()
                .filter { it is WebSocket.Event.OnConnectionOpened<*> }
                .subscribe({
                    blockchainService.sendSubscribe(Subscribe())
                }, {
                    Log.e("Error", it.message)
                })
    }

    fun getTransactionObservable(): Flowable<Unconfirmed> {
        return blockchainService.observeTicker()
    }
}
