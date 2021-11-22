package com.github.kovah101.chargemycar.savedChargePoints

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.github.kovah101.chargemycar.savedDatabase.ChargeDatabaseDAO

class SavedPointsViewModel(
    val database: ChargeDatabaseDAO,
    application: Application
) : AndroidViewModel(application) {
}