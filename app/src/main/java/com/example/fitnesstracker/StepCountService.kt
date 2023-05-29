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

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        stepSensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        sensorManager!!.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onDestroy() {
        super.onDestroy()

        sensorManager!!.unregisterListener(this)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_STEP_COUNTER) {
            val steps = event.values[0]
            val intent = Intent("STEP_COUNT_UPDATE")
            intent.putExtra("steps", steps)
            sendBroadcast(intent)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {

    }
}