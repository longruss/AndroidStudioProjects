package com.example.item_sensor_application

import android.content.Context
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
    private val sensorDataList: MutableMap<Int, MutableList<Float>> = mutableMapOf()

    private val sensorIds = listOf(
        Sensor.TYPE_ACCELEROMETER,
        Sensor.TYPE_GYROSCOPE,
        Sensor.TYPE_MAGNETIC_FIELD
        // Add more sensor types as needed
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        // Initialize the sensor data list
        for (sensorId in sensorIds) {
            sensorDataList[sensorId] = mutableListOf()
        }
    }

    override fun onResume() {
        super.onResume()
        // Register sensor listeners for all sensor types
        for (sensorId in sensorIds) {
            val sensor = sensorManager.getDefaultSensor(sensorId)
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }

        // Start updating the TextViews every 10 seconds
        updateTextViews()
    }

    override fun onPause() {
        super.onPause()
        // Unregister sensor listeners
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Do nothing
    }

    override fun onSensorChanged(event: SensorEvent) {
        val sensorId = event.sensor.type
        val sensorValues = event.values

        // Update the sensor data list
        val dataList = sensorDataList[sensorId]
        if (dataList != null) {
            dataList.add(sensorValues[0])
        }
    }

    private fun updateTextViews() {
        for (sensorId in sensorIds) {
            val sensorNameTextView = getSensorNameTextView(sensorId)
            val sensorValueTextView = getSensorValueTextView(sensorId)

            val dataList = sensorDataList[sensorId]
            if (dataList != null && dataList.size >= 10) {
                val lastTenValues = dataList.takeLast(10)
                val sensorName = getSensorName(sensorId)
                val sensorValuesText = lastTenValues.joinToString("\n")

                sensorNameTextView.text = sensorName
                sensorValueTextView.text = sensorValuesText
            }
        }

        // Schedule the next update after 10 seconds
        Handler().postDelayed({ updateTextViews() }, 5000)
    }

    private fun getSensorName(sensorId: Int): String {
        return when (sensorId) {
            Sensor.TYPE_ACCELEROMETER -> "Accelerometer"
            Sensor.TYPE_GYROSCOPE -> "Gyroscope"
            Sensor.TYPE_MAGNETIC_FIELD -> "Magnetic Field"
            // Add more sensor types as needed
            else -> "Unknown"
        }
    }

    private fun getSensorNameTextView(sensorId: Int): TextView {
        return when (sensorId) {
            Sensor.TYPE_ACCELEROMETER -> findViewById(R.id.sensor1NameTextView)
            Sensor.TYPE_GYROSCOPE -> findViewById(R.id.sensor2NameTextView)
            Sensor.TYPE_MAGNETIC_FIELD -> findViewById(R.id.sensor3NameTextView)
            // Add more sensor types as needed
            else -> throw IllegalArgumentException("Invalid sensor ID")
        }
    }

    private fun getSensorValueTextView(sensorId: Int): TextView {
        return when (sensorId) {
            Sensor.TYPE_ACCELEROMETER -> findViewById(R.id.sensor1ValueTextView)
            Sensor.TYPE_GYROSCOPE -> findViewById(R.id.sensor2ValueTextView)
            Sensor.TYPE_MAGNETIC_FIELD -> findViewById(R.id.sensor3ValueTextView)
            // Add more sensor types as needed
            else -> throw IllegalArgumentException("Invalid sensor ID")
        }
    }
}
