package com.ardhix.batterytoolkit

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build

/**
 * Main entry point for Android Battery Toolkit.
 *
 * Provides battery information exposed by the Android system.
 */
object BatteryToolkit {

    /**
     * Reads the current battery information from the device.
     */
    fun getInfo(context: Context): BatteryInfo {
        val batteryManager =
            context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager

        val batteryIntent = context.registerReceiver(
            null,
            IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        )

        val level = batteryIntent?.getIntExtra(
            BatteryManager.EXTRA_LEVEL,
            -1
        ) ?: -1

        val scale = batteryIntent?.getIntExtra(
            BatteryManager.EXTRA_SCALE,
            100
        ) ?: 100

        val percentage = if (level >= 0 && scale > 0) {
            ((level * 100f) / scale).toInt().coerceIn(0, 100)
        } else {
            -1
        }

        val status = batteryIntent?.getIntExtra(
            BatteryManager.EXTRA_STATUS,
            BatteryManager.BATTERY_STATUS_UNKNOWN
        ) ?: BatteryManager.BATTERY_STATUS_UNKNOWN

        val isCharging =
            status == BatteryManager.BATTERY_STATUS_CHARGING ||
            status == BatteryManager.BATTERY_STATUS_FULL

        val temperatureRaw = batteryIntent?.getIntExtra(
            BatteryManager.EXTRA_TEMPERATURE,
            Int.MIN_VALUE
        ) ?: Int.MIN_VALUE

        val temperatureCelsius =
            if (temperatureRaw != Int.MIN_VALUE) {
                temperatureRaw / 10f
            } else {
                null
            }

        val voltageRaw = batteryIntent?.getIntExtra(
            BatteryManager.EXTRA_VOLTAGE,
            Int.MIN_VALUE
        ) ?: Int.MIN_VALUE

        val voltageVolts =
            if (voltageRaw != Int.MIN_VALUE) {
                voltageRaw / 1000f
            } else {
                null
            }

        val currentMicroAmps =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                runCatching {
                    batteryManager.getIntProperty(
                        BatteryManager.BATTERY_PROPERTY_CURRENT_NOW
                    )
                }.getOrNull()?.takeIf { it != Int.MIN_VALUE }
            } else {
                null
            }

        val technology = batteryIntent?.getStringExtra(
            BatteryManager.EXTRA_TECHNOLOGY
        )

        val health = batteryIntent?.getIntExtra(
            BatteryManager.EXTRA_HEALTH,
            BatteryManager.BATTERY_HEALTH_UNKNOWN
        ) ?: BatteryManager.BATTERY_HEALTH_UNKNOWN

        val chargingSource = getChargingSource(batteryIntent)

        return BatteryInfo(
            percentage = percentage,
            isCharging = isCharging,
            temperatureCelsius = temperatureCelsius,
            voltageVolts = voltageVolts,
            currentMicroAmps = currentMicroAmps,
            technology = technology,
            health = health,
            chargingSource = chargingSource
        )
    }

    private fun getChargingSource(intent: Intent?): String? {
        val plugged = intent?.getIntExtra(
            BatteryManager.EXTRA_PLUGGED,
            0
        ) ?: 0

        return when {
            plugged and BatteryManager.BATTERY_PLUGGED_USB != 0 -> "USB"
            plugged and BatteryManager.BATTERY_PLUGGED_AC != 0 -> "AC"
            plugged and BatteryManager.BATTERY_PLUGGED_WIRELESS != 0 -> "Wireless"
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 &&
                plugged and BatteryManager.BATTERY_PLUGGED_DOCK != 0 -> "Dock"
            else -> null
        }
    }
}
