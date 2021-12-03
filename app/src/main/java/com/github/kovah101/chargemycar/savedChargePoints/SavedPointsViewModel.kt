package com.github.kovah101.chargemycar.savedChargePoints

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.github.kovah101.chargemycar.formatChargePoints
import com.github.kovah101.chargemycar.savedDatabase.ChargeDatabaseDAO
import com.github.kovah101.chargemycar.savedDatabase.ChargePoint
import kotlinx.coroutines.launch

class SavedPointsViewModel(
    val database: ChargeDatabaseDAO,
    application: Application
) : AndroidViewModel(application) {

    //TODO Add & remove single charge point on recyclerview item click!

    private val chargePoints = database.getAllPoints()

    private var dummyData = mutableListOf<ChargePoint>()

    //convert all favourite charge points to string for ScrollView
    val chargePointsString = Transformations.map(chargePoints) { chargePoints ->
        formatChargePoints(chargePoints, application.resources)
    }

    // Add all the dummy data to the database
    private suspend fun addAll(dummyData : List<ChargePoint>){
        database.insertAll(dummyData)
    }

    // Clear all data from the database
    private suspend fun clearAll(){
        database.clear()
    }

    // Add new charge point
    private suspend fun addFavourite(chargePoint: ChargePoint){
        database.insert(chargePoint)
    }

    // Remove charge point
    private suspend fun removeFavourite(chargePoint: ChargePoint){
        database.removePoint(chargePoint.chargePointId)
    }

    // Executes when ADD DATA button pressed
    // inserts dummy data then displays in scrollView
    fun addData(){
        viewModelScope.launch {
            addAll(dummyData)
            //update chargePoint strings?
        }
    }

    // Executes when CLEAR DATA button pressed
    fun clearData(){
        viewModelScope.launch {
            clearAll()
        }
    }

}