package com.acous.airqualityindex.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.Exception


class CityList(val list: String?=null, val exception: Throwable?=null) {
    val type = object : TypeToken<List<CityAqi>?>() {}.type
    var citiesList = Gson().fromJson<List<CityAqi>>(list, type)
}