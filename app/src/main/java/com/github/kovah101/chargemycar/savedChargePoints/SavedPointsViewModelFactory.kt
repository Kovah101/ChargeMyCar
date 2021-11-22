package com.github.kovah101.chargemycar.savedChargePoints

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.github.kovah101.chargemycar.savedDatabase.ChargeDatabaseDAO
import java.lang.IllegalArgumentException

class SavedPointsViewModelFactory(
    private val dataSource: ChargeDatabaseDAO,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SavedPointsViewModel::class.java)) {
            return SavedPointsViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}