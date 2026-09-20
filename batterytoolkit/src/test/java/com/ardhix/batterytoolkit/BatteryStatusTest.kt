package com.ardhix.batterytoolkit

import android.os.BatteryManager
import org.junit.Assert.assertEquals
import org.junit.Test

class BatteryStatusTest {

    @Test
    fun chargingStatus_returnsCharging() {
        assertEquals(
            "Charging",
            BatteryStatus.statusName(
                BatteryManager.BATTERY_STATUS_CHARGING
            )
        )
    }

    @Test
    fun dischargingStatus_returnsDischarging() {
        assertEquals(
            "Discharging",
            BatteryStatus.statusName(
                BatteryManager.BATTERY_STATUS_DISCHARGING
            )
        )
    }

    @Test
    fun fullStatus_returnsFull() {
        assertEquals(
            "Full",
            BatteryStatus.statusName(
                BatteryManager.BATTERY_STATUS_FULL
            )
        )
    }

    @Test
    fun goodHealth_returnsGood() {
        assertEquals(
            "Good",
            BatteryStatus.healthName(
                BatteryManager.BATTERY_HEALTH_GOOD
            )
        )
    }

    @Test
    fun overheatHealth_returnsOverheat() {
        assertEquals(
            "Overheat",
            BatteryStatus.healthName(
                BatteryManager.BATTERY_HEALTH_OVERHEAT
            )
        )
    }

    @Test
    fun unknownStatus_returnsUnknown() {
        assertEquals(
            "Unknown",
            BatteryStatus.statusName(-999)
        )
    }

    @Test
    fun unknownHealth_returnsUnknown() {
        assertEquals(
            "Unknown",
            BatteryStatus.healthName(-999)
        )
    }
}
