package com.sal7one.serversocket.data

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.activity.ComponentActivity
import com.sal7one.serversocket.di.SensorData
import kotlinx.coroutines.channels.Channel

class AppSensorManager(appContext: Context) : SensorEventListener {

    private val sensorManager: SensorManager =
        appContext.getSystemService(ComponentActivity.SENSOR_SERVICE) as SensorManager

    val sensorChannel = Channel<SensorData>(Channel.UNLIMITED)

    init {
        registerSensor(Sensor.TYPE_LIGHT)
        registerSensor(Sensor.TYPE_ACCELEROMETER)
    }

    private fun registerSensor(sensorType: Int) {
        val sensor = sensorManager.getDefaultSensor(sensorType)
        sensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.apply {
            val cleanData = event.values[0].toString().replace("[^\\d.]".toRegex(), "")
            when (this.sensor.type) {
                Sensor.TYPE_LIGHT -> sensorChannel.trySend(SensorData(luminosity = "Luminosity: $cleanData"))
                Sensor.TYPE_ACCELEROMETER ->
                    sensorChannel.trySend(SensorData(accelerometer = "Accelerometer: $cleanData"))
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    fun cancel() {
        sensorManager.unregisterListener(this)
    }
}