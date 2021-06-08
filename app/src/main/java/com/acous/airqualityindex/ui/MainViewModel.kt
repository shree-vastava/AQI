package com.acous.airqualityindex.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.acous.airqualityindex.data.CityAqi
import com.acous.airqualityindex.data.Repository
import com.acous.airqualityindex.util.Resource
import com.acous.airqualityindex.util.Status
import com.github.marlonlom.utilities.timeago.TimeAgo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.time.Duration


class MainViewModel @ViewModelInject constructor(private val repository: Repository): ViewModel() {

    var citiesListAqi = repository.getCitiesList()
    //var citiesList = repository.getCitiesList()

    init {
       // subscribeToSocketEvents()
    }

    fun subscribeToSocketEvents() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.webSocketCreate(viewModelScope).collect {
                when (it.status) {
                    Status.SUCCESS -> {
                        it.data?.citiesList?.let { it1 -> repository.saveCityData(it1) }
                    }
                    Status.ERROR -> {

                    }
                }
            }

        }
    }

    fun closeWebSocket(){
        repository.closeWebSocket()
    }
}
