package com.sal7one.serversocket

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.activity.ComponentActivity
import kotlinx.coroutines.channels.Channel

class SensorHandler(context: Context) : SensorEventListener {

    private var sensorManager: SensorManager
    val lightData: Channel<SensorData> = Channel(Channel.UNLIMITED)
    val acellData: Channel<SensorData> = Channel(Channel.UNLIMITED)

    init {
        sensorManager = context.getSystemService(ComponentActivity.SENSOR_SERVICE) as SensorManager
        val lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        val accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_UI)
        sensorManager.registerListener(this, accelSensor, SensorManager.SENSOR_DELAY_UI)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            if (event.sensor.type == Sensor.TYPE_LIGHT) {
                val cleanData =  event.values[0].toString().replace(("[^\\d.]").toRegex(), "")
                lightData.trySend(
                    SensorData(
                        "Luminosity: $cleanData"
                    )
                )
            }else if (event.sensor.type == Sensor.TYPE_ACCELEROMETER){
                val cleanData =  event.values[0].toString().replace(("[^\\d.]").toRegex(), "")
                acellData.trySend(
                    SensorData(
                        accelerometer = "Accelerometer: $cleanData"
                    )
                )
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    fun cancel() {
        sensorManager.unregisterListener(this)
    }
}