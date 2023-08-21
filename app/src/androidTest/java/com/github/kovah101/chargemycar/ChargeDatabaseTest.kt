package com.github.kovah101.chargemycar

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.github.kovah101.chargemycar.savedDatabase.ChargeDatabase
import com.github.kovah101.chargemycar.savedDatabase.ChargeDatabaseDAO
import com.github.kovah101.chargemycar.savedDatabase.ChargePoint
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.assertEquals
import java.io.IOException


@RunWith(AndroidJUnit4::class)
class ChargeDatabaseTest {

    private lateinit var chargeDao: ChargeDatabaseDAO
    private lateinit var db: ChargeDatabase

    // initially create in-memory database for testing, as info stored here disappears when process dies
    @Before
    fun createDb(){
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        db = Room.inMemoryDatabaseBuilder(context, ChargeDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        chargeDao = db.chargeDatabaseDAO
    }

    // finally close the database
    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    // simple test to check insert & getPointByPostcode Queries
    @Test
    @Throws(Exception::class)
    suspend fun insertAndGetPoint(){
        val point = ChargePoint(postcode = "Tester")
        chargeDao.insert(point)
        val currentPoint = chargeDao.getPointByPostcode("Tester")
        assertEquals(currentPoint?.latitude, 0F)
    }
}