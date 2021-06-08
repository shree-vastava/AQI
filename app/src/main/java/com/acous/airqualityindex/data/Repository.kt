package com.acous.airqualityindex.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.acous.airqualityindex.network.IWebSocketChannel
import com.acous.airqualityindex.network.WebSocketChannel
import com.acous.airqualityindex.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class Repository @Inject constructor(private val aqiDao: AqiDao) {

    private lateinit var channel: IWebSocketChannel

    fun webSocketCreate(scope: CoroutineScope): Flow<Resource<CityList>> {
        channel = WebSocketChannel(scope)
        return channel.getIncoming()
    }

    fun webSocketSend(data: CityList) {
        channel.send(data)
    }

    fun closeWebSocket(){
        channel.close()
    }

    suspend fun saveCityData(cities: List<CityAqi>){
        cities?.map { it.setColor() }
        cities?.let {
            aqiDao.insert(cities)
        }
    }

   fun getCitiesList():LiveData<Resource<List<CityAqi>>> =
        liveData(Dispatchers.IO) {
            emit(Resource.loading(null))
            val source = { aqiDao.getCitiesData() }.invoke().map { Resource.success(it) }
            emitSource(source)
        }

}