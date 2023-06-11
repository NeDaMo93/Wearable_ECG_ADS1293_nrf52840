package no.nordicsemi.android.blinky.ble.data

import android.bluetooth.BluetoothDevice

class IMU_temp_Data: IMU_Temp_Callback() {
    var IMU_temp_Data: UIntArray = uintArrayOf(0u, 0u, 0u, 0u, 0u, 0u, 0u)
    override fun onRecievedDataPacket(device: BluetoothDevice, IMU_temp_Data: UIntArray) {
        this.IMU_temp_Data = IMU_temp_Data
    }
}