package com.ardhix.batterytoolkit

/**
 * Represents battery information reported by the Android system.
 *
 * Some fields may be null because Android or the device manufacturer
 * does not expose every battery metric on every device.
 */
data class BatteryInfo(
    val percentage: Int,
    val isCharging: Boolean,
    val temperatureCelsius: Float?,
    val voltageVolts: Float?,
    val currentMicroAmps: Int?,
    val technology: String?,
    val health: Int,
    val chargingSource: String?
)
