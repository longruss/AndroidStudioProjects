package com.example.item_sensor_application

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView


data class SensorData(val sensorName: String, val sensorValue: Float)

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private val sensorDataList = mutableMapOf<Int, MutableList<Float>>()
    private val handler = Handler(Looper.getMainLooper())
    private val updateIntervalMillis: Long = 5000 // 5 seconds

    private val sensorIds = arrayOf(
        Sensor.TYPE_ACCELEROMETER,
        Sensor.TYPE_GYROSCOPE,
        Sensor.TYPE_MAGNETIC_FIELD
    )
    // Add more sensor types as needed

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        // Initialize the sensorDataList with empty lists for each sensor
        for (sensorId in sensorIds) {
            sensorDataList[sensorId] = mutableListOf()
        }
    }

    override fun onResume() {
        super.onResume()
        startSensorUpdates()
        startDataUpdates()
    }

    override fun onPause() {
        super.onPause()
        stopSensorUpdates()
        stopDataUpdates()
    }

    private fun startSensorUpdates() {
        for (sensorId in sensorIds) {
            val sensor = sensorManager.getDefaultSensor(sensorId)
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    private fun stopSensorUpdates() {
        sensorManager.unregisterListener(this)
    }

    private fun startDataUpdates() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                updateData()
                handler.postDelayed(this, updateIntervalMillis)
            }
        }, updateIntervalMillis)
    }

    private fun stopDataUpdates() {
        handler.removeCallbacksAndMessages(null)
    }

    private fun updateData() {
        for (sensorId in sensorIds) {
            val sensorData = sensorDataList[sensorId]
            val latestData = sensorData?.takeLast(10) ?: emptyList()
            updateTextViews(sensorId, latestData)
        }
    }

    private fun updateTextViews(sensorId: Int, data: List<Float>) {
        val sensorNameTextView = getSensorNameTextView(sensorId)
        val sensorValueTextView = getSensorValueTextView(sensorId)

        // Set the sensor name
        sensorNameTextView?.text = getSensorName(sensorId)

        // Set the sensor values
        val formattedValues = data.joinToString(", ")
        sensorValueTextView?.text = formattedValues
    }

    private fun getSensorNameTextView(sensorId: Int): TextView? {
        val resourceId = getSensorNameResourceId(sensorId)
        return findViewById(resourceId)
    }

    private fun getSensorValueTextView(sensorId: Int): TextView? {
        val resourceId = getSensorValueResourceId(sensorId)
        return findViewById(resourceId)
    }

    private fun getSensorName(sensorId: Int): String {
        return when (sensorId) {
            Sensor.TYPE_ACCELEROMETER -> "Accelerometer"
            Sensor.TYPE_GYROSCOPE -> "Gyroscope"
            Sensor.TYPE_MAGNETIC_FIELD -> "Magnetic Field"
            // Add more cases for additional sensor types
            else -> "Unknown"
        }
    }

    private fun getSensorNameResourceId(sensorId: Int): Int {
        return when (sensorId) {
            Sensor.TYPE_ACCELEROMETER -> R.id.sensor1NameTextView
            Sensor.TYPE_GYROSCOPE -> R.id.sensor2NameTextView
            Sensor.TYPE_MAGNETIC_FIELD -> R.id.sensor3NameTextView
            // Add more cases for additional sensor types
            else -> -1
        }
    }

    private fun getSensorValueResourceId(sensorId: Int): Int {
        return when (sensorId) {
            Sensor.TYPE_ACCELEROMETER -> R.id.sensor1ValueTextView
            Sensor.TYPE_GYROSCOPE -> R.id.sensor2ValueTextView
            Sensor.TYPE_MAGNETIC_FIELD -> R.id.sensor3ValueTextView
            // Add more cases for additional sensor types
            else -> -1
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Ignored
    }

    override fun onSensorChanged(event: SensorEvent) {
        val sensorId = event.sensor.type
        val sensorData = sensorDataList[sensorId]
        sensorData?.apply {
            add(event.values[0])
            if (size > 10) {
                removeAt(0)
            }
        }
    }
}