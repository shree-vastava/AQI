package com.acous.airqualityindex.ui

import android.os.CountDownTimer
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acous.airqualityindex.data.CityAqi
import com.acous.airqualityindex.data.Repository
import com.acous.airqualityindex.util.Resource
import com.acous.airqualityindex.util.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ChartViewModel @ViewModelInject constructor(private val repository: Repository): ViewModel() {

    var aqiList = MutableLiveData<Resource<Float>>()
    var delayValue:Long = 0

    fun subscribeToSocketEvents(cityName: String) {

        /**
         * Initializing the websocket
         * class in repository
         */

        viewModelScope.launch(Dispatchers.IO) {
            repository.webSocketCreate(viewModelScope).collect {
                when (it.status) {

                    Status.ERROR -> {
                        aqiList.postValue(Resource.error(it.message!!, null))
                    }

                    Status.SUCCESS -> {
                        /**
                         * Updating the UI only when
                         * the data is received for the selected
                         * city.
                         */
                        var selectedCity = it.data!!.citiesList.find { it.city == cityName }
                        var aqiForSelectedCity = selectedCity?.aqi ?: 0.0f
                        if (aqiForSelectedCity > 0.0f) {
                            aqiList.postValue(Resource.success(aqiForSelectedCity))
                        }
                        else{
                            aqiList.postValue(Resource.error("No new value found, Please wait for next value", null))
                        }
                       delay(delayValue)
                    }
                }

            }

        }
    }


    /**
     * Setting up countdowntimer
     * ot show on UI
     */


    fun setDelay(delay:Long){
        delayValue = delay*1000
    }

    fun closeWebSocket(){
        repository.closeWebSocket()
    }

    override fun onCleared() {
        super.onCleared()
    }

}
