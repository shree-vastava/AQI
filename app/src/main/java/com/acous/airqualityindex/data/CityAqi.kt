package com.acous.airqualityindex.data

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.acous.airqualityindex.R
import kotlinx.android.parcel.Parcelize

@Parcelize @Entity
data class CityAqi(
    @PrimaryKey @NonNull
    val city: String,
    val aqi: Float,
    var lastSyncTime: Long = System.currentTimeMillis(),
    var colorValue: Int?=null,

): Parcelable{

    fun setColor(){
        lastSyncTime = System.currentTimeMillis()

        if(aqi!!>0 && aqi<50){
            colorValue = R.color.aqi_good //Good
        }
        else if(aqi>50 && aqi<100){
            colorValue = R.color.aqi_satistactory //Satisfactory
        }
        else if(aqi>100 && aqi<200){
            colorValue = R.color.aqi_moderate //moderate
        }
        else if(aqi>200 && aqi<300){
            colorValue = R.color.aqi_poor //Poor
        }
        else if(aqi>300 && aqi<400){
            colorValue = R.color.aqi_vpoor //Vpoor
        }
        else if(aqi>400 && aqi<500){
            colorValue = R.color.aqi_severe //Severe
        }
    }
}