package com.github.kovah101.chargemycar.viewModel

import android.app.Application
import android.text.TextUtils
import android.widget.RadioGroup
import androidx.lifecycle.*
import com.github.kovah101.chargemycar.convertChargePoints
import com.github.kovah101.chargemycar.formatChargePoints
import com.github.kovah101.chargemycar.network.ChargeApi
import com.github.kovah101.chargemycar.savedDatabase.ChargeDatabase
import com.github.kovah101.chargemycar.savedDatabase.ChargePoint
import kotlinx.coroutines.launch
import timber.log.Timber

enum class ChargeQueryAPIStatus {LOADING, DONE, ERROR}

class ChargePointViewModel(application: Application) : AndroidViewModel(application) {

    //application object for database
    //private val application : Application = requireNotNull(this.activity).application
    // create view model & bind the UI to it
    val database = ChargeDatabase.getInstance(application).chargeDatabaseDAO
    //val database = dataSource

    val chargePoints = database.getAllPoints()

    private var dummyData = mutableListOf<ChargePoint>()

    private val dummy = ChargePoint()
    private val dummy1 = ChargePoint()
    private val dummy2 = ChargePoint()
    private val dummy3 = ChargePoint()
    private val dummy4 = ChargePoint()
    private val dummy5 = ChargePoint()
    private val dummy6 = ChargePoint()
    private val dummy7 = ChargePoint()
    private val dummy8 = ChargePoint()
    private val dummy9 = ChargePoint()
    private val dummy10 = ChargePoint()


    //convert all favourite charge points to string for ScrollView
    val chargePointsString = Transformations.map(chargePoints) { chargePoints ->
        formatChargePoints(chargePoints, application.resources)
    }

    // Add all the dummy data to the database
    private suspend fun addAll(dummyData: List<ChargePoint>) {
        database.insertAll(dummyData)
    }

    // Clear all data from the database
    private suspend fun clearAll() {
        database.clear()
    }

    // Add new charge point
    private suspend fun addFavourite(chargePoint: ChargePoint) {
        database.insert(chargePoint)
    }

    // Remove charge point
    private suspend fun removeFavourite(chargePoint: ChargePoint) {
        database.removePoint(chargePoint.chargePointId)
    }

    // find charge point by ID
    private suspend fun findPoint(chargeID: Long): ChargePoint? {
        return database.getPoint(chargeID)
    }

    // Find charge point by postcode
    private suspend fun findPointByPostcode(postcode: String): ChargePoint? {
        return database.getPointByPostcode(postcode)
    }

    // Find charge point by latitude & longitude
    private suspend fun findPointByLatAndLong(lat: String, long: String): ChargePoint? {
        return database.getPointByLatAndLong(lat, long)
    }

    private suspend fun removeChargePoint(key: Long) {
        database.removePoint(key)
    }

    // Executes when ADD DATA button pressed
    // inserts dummy data then displays in scrollView
    fun addData() {
        viewModelScope.launch {
            Timber.d("Charge Points has: ${chargePoints.value?.size} elements")
            generateDummyData()
            addAll(dummyData)
            Timber.d("Data Added!")
            Timber.d("Charge Points has: ${chargePoints.value?.size} elements")
            //update chargePoint strings?
        }
    }

    // Executes when CLEAR DATA button pressed
    fun clearData() {
        viewModelScope.launch {
            dummyData.clear()
            clearAll()
            _showSnackBarEvent.value = true
            Timber.d("Data Cleared")
            Timber.d("Charge Points has: ${chargePoints.value?.size} elements")
        }
    }

    // adds and fills in dummy charge points to list
    private fun generateDummyData() {
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

            it.latitude = "51.471114"
            it.longitude = "-0.1083977"
            it.chargePointStatus = "In service"
            it.postcode = "SW9 6SU"
            it.connectorType = "Type 2 Mennekes"
            it.locationType = "$i"
            i++
        }
    }

    // SnackBar message practise
    // private mutable variable connected to public non mutable
    private var _showSnackBarEvent = MutableLiveData<Boolean>()

    val showSnackBarEvent: LiveData<Boolean>
        get() = _showSnackBarEvent

    fun doneShowingSnackBar() {
        _showSnackBarEvent.value = false
    }

    // Find charge point by latitude & longitude and delete it
    fun findAndRemoveChargePoint(chargeID: Long) {
        viewModelScope.launch {
            val point = findPoint(chargeID)
            if (point != null) {
                Timber.d("Removing Charge Point :${point.chargePointId}")
                removeChargePoint(point.chargePointId)
            }
        }
    }

    // Find charge point, if not there then adds new one
    fun addIfNewChargePoint(point: ChargePoint) {
        viewModelScope.launch {
            val chargePoint = findPointByLatAndLong(point.latitude, point.longitude)
            Timber.d("Charge Point = $chargePoint")
            if (chargePoint == null) {
                Timber.d("Adding new Favourite")
                addFavourite(point)
            }
        }
    }

    // The internal MutableLiveData String that stores the most recent response
    private val _response = MutableLiveData<String>()

    // The external immutable LiveData for the response String
    val response: LiveData<String>
        get() = _response

    // internal live data that stores the list of charge points from the internet
    private val _listOfChargePoints = MutableLiveData<List<ChargePoint>>()

    // external immutable live data of charge points from the internet
    val listOfChargePoints: LiveData<List<ChargePoint>>
        get() = _listOfChargePoints

    // internal MutableLiveData Boolean that flags a successful response
    private val _status = MutableLiveData<ChargeQueryAPIStatus>()

    // external immutable LiveData for success boolean
    val status: LiveData<ChargeQueryAPIStatus>
        get() = _status

    // internal live data that stores the distance parameter for query search, default to 10
    val distance = MutableLiveData<String>("10")
    //private val distance = "10"

    // internal live data that stores the limit parameter for query search, default to 10
    val limit = MutableLiveData<String>("10")

    fun radioGroupTest(radioGroup: RadioGroup, id: Int){
        Timber.d("Radiobutton: $id has been clicked")
    }


    fun getChargePointQuery() {
        viewModelScope.launch {
            _status.value = ChargeQueryAPIStatus.LOADING
            try {
                var chargeQuery = ChargeApi.retrofitService.getChargeQueryObject(distance.value.toString(),limit.value.toString())
                _listOfChargePoints.value = convertChargePoints(chargeQuery.chargeDevices)
                _status.value = ChargeQueryAPIStatus.DONE
               // var responseString = chargeQuery.scheme.SchemeCode
                val responseStringList = listOfChargePoints.value?.let { TextUtils.join(",", it) }
                _response.value = responseStringList!!

            } catch (e: Exception) {
                _status.value = ChargeQueryAPIStatus.ERROR
                _response.value = "Failure: ${e.message}"

            }
        }
    }

}