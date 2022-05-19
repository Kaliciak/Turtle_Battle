package com.kaliciak.turtlebattle.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kaliciak.turtlebattle.databinding.FoundGameListItemBinding
import com.kaliciak.turtlebattle.model.bluetooth.Device
import com.kaliciak.turtlebattle.view.GameJoinActivityDelegate

class FoundGamesRecViewAdapter(var delegate: GameJoinActivityDelegate):
RecyclerView.Adapter<FoundGamesRecViewAdapter.ViewHolder>() {

    private var deviceList: List<Device> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FoundGameListItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(deviceList[position])
    }

    override fun getItemCount(): Int = deviceList.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(deviceList: List<Device>) {
        this.deviceList = deviceList
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: FoundGameListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val deviceName: TextView = binding.deviceName
        val deviceMAC: TextView = binding.deviceMAC

        fun bind(device: Device) {
            deviceName.text = device.name
            deviceMAC.text = device.mac

            binding.joinGameButton.setOnClickListener {
                delegate.joinDevice(device.mac)
            }
        }
    }

}