package com.example.umangburman.databindingwithlivedata.repository

import android.util.Log
import com.example.umangburman.databindingwithlivedata.api.BlockchainService
import com.example.umangburman.databindingwithlivedata.api.Subscribe
import com.example.umangburman.databindingwithlivedata.api.Unconfirmed
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
