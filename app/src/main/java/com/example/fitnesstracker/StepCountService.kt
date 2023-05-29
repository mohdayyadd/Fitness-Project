package com.example.fitnesstracker

import android.app.Service
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder


class StepCountService : Service(), SensorEventListener {
    private var sensorManager: SensorManager? = null
    private var stepSensor: Sensor? = null
    override fun onCreate() {
        super.onCreate()

        // Initialize the sensor manager
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        // Get the step counter sensor
        stepSensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        // Register the sensor listener
        sensorManager!!.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onDestroy() {
        super.onDestroy()

        // Unregister the sensor listener
        sensorManager!!.unregisterListener(this)
    }

    override fun onBind(intent: Intent): IBinder? {
        // We don't need to bind to this service, so return null
        return null
    }

    override fun onSensorChanged(event: SensorEvent) {
        // Handle the sensor event here
        if (event.sensor.type == Sensor.TYPE_STEP_COUNTER) {
            val steps = event.values[0]
            // You can do something with the steps count, such as update a database or send it to the main activity
            // For example, you can use an Intent to send a broadcast with the steps count
            val intent = Intent("STEP_COUNT_UPDATE")
            intent.putExtra("steps", steps)
            sendBroadcast(intent)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // Handle accuracy changes if needed
    }
}