package com.example.item_sensor_application

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SensorDataAdapter (private val sensorDataList: List<SensorData>) :
    RecyclerView.Adapter<SensorDataAdapter.SensorDataViewHolder>(){
    class SensorDataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sensorNameTextView: TextView = itemView.findViewById(R.id.sensorNameTextView)
        val sensorValueTextView: TextView = itemView.findViewById(R.id.sensorValueTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SensorDataViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_sensor_data, parent, false)
        return SensorDataViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SensorDataViewHolder, position: Int) {
        val currentData = sensorDataList[position]
        holder.sensorNameTextView.text = currentData.sensorName
        holder.sensorValueTextView.text = currentData.sensorValue.toString()
    }

    override fun getItemCount() = sensorDataList.size
}