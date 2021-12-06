package com.github.kovah101.chargemycar.savedChargePoints

import android.app.Application
import androidx.lifecycle.*
import com.github.kovah101.chargemycar.formatChargePoints
import com.github.kovah101.chargemycar.savedDatabase.ChargeDatabaseDAO
import com.github.kovah101.chargemycar.savedDatabase.ChargePoint
import kotlinx.coroutines.launch
import timber.log.Timber

class SavedPointsViewModel(
    val database: ChargeDatabaseDAO,
    application: Application
) : AndroidViewModel(application) {

    //TODO Add & remove single charge point on recyclerview item click!

    private val chargePoints = database.getAllPoints()

    private var dummyData = mutableListOf<ChargePoint>()

    private val dummy = ChargePoint()
    private val dummy1 = ChargePoint()
    private val dummy2 = ChargePoint()
    private val dummy3 = ChargePoint()
    private val dummy4 = ChargePoint()
    private val dummy5 = ChargePoint()
    private val dummy6= ChargePoint()
    private val dummy7 = ChargePoint()
    private val dummy8 = ChargePoint()
    private val dummy9 = ChargePoint()
    private val dummy10 = ChargePoint()



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
            generateDummyData()
            addAll(dummyData)
            //update chargePoint strings?
        }
    }

    // Executes when CLEAR DATA button pressed
    fun clearData(){
        viewModelScope.launch {
            dummyData.clear()
            clearAll()
            _showSnackBarEvent.value = true
        }
    }

    // adds and fills in dummy charge points to list
    private fun generateDummyData(){
        dummyData.add(dummy)
        dummyData.add(dummy1)
        dummyData.add(dummy2)
        dummyData.add(dummy3)
        dummyData.add(dummy4)
        dummyData.add(dummy5)
        dummyData.add(dummy6)
        dummyData.add(dummy7)
        dummyData.add(dummy8)
        dummyData.add(dummy9)
        dummyData.add(dummy10)

        var i = 0

        dummyData.forEach {

            it.latitude = 51.4532F
            it.longitude = -0.568332F
            it.chargePointStatus = true
            it.postcode = "SW99QC"
            it.connectorType = "Type 2 Mennekes"
            it.locationType = "$i"
            i++
        }
    }

    // SnackBar message practise
    // private mutable variable connected to public non mutable
    private var _showSnackBarEvent = MutableLiveData<Boolean>()

    val showSnackBarEvent : LiveData<Boolean>
        get() = _showSnackBarEvent

    fun doneShowingSnackBar(){
        _showSnackBarEvent.value = false
    }


}