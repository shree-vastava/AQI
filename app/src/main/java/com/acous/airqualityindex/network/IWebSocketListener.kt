package com.acous.airqualityindex.network

import com.acous.airqualityindex.data.CityList
import com.acous.airqualityindex.util.Resource
import kotlinx.coroutines.flow.Flow

interface IWebSocketChannel {
    fun getIncoming(): Flow<Resource<CityList>>
    fun isClosed(): Boolean
    fun close(
            code: Int = 1000,
            reason: String? = null
    )

    fun send(data: CityList)
}