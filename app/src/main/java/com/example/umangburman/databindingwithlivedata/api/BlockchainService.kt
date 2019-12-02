package com.example.umangburman.databindingwithlivedata.api

import com.squareup.moshi.Json
import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.ws.Receive
import com.tinder.scarlet.ws.Send
import io.reactivex.Flowable
import java.util.*


interface BlockchainService {
    @Receive
    fun observeWebSocketEvent(): Flowable<WebSocket.Event>
    @Send
    fun sendSubscribe(subscribe: Subscribe)
    @Receive
    fun observeTicker(): Flowable<Ticker>
}


data class Subscribe(
    val op: String = "unconfirmed_sub"
)

data class Ticker(
        val op: String,
        val x: XData
)

data class XData(
        @Json(name = "time")
        val time: Long,
        val out: List<OutData>
) {
    val datetime: Date
        get() = Date(time)

    val total: Long
        get() {
            var sum = 0L
            for (element in out) {
                sum += element.value
            }
            return sum
        }
}

data class OutData(
        val spent: Boolean,
        val value: Long
)
