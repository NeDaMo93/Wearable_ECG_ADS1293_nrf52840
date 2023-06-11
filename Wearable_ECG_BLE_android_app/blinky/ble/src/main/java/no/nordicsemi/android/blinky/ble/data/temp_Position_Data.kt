package no.nordicsemi.android.blinky.ble.data

import android.bluetooth.BluetoothDevice

class temp_Position_Data: Temperature_Position_Callback() {
    var temp_Position_Data: UIntArray = uintArrayOf(0u, 0u)
    override fun onRecievedDataPacket(device: BluetoothDevice, temp_Position_Data: UIntArray) {
        this.temp_Position_Data = temp_Position_Data
    }
}