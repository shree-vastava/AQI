package com.acous.airqualityindex.util

import android.widget.TextView
import com.acous.airqualityindex.data.CityAqi

interface CityClickListener {
    fun onCityClicked(cityAqi: CityAqi, tvCityName: TextView)
}