package no.nordicsemi.android.blinky.ble.data

import android.bluetooth.BluetoothDevice

class HeartRate: HeartRateCallback() {
    var heartRate: UIntArray = uintArrayOf(0u, 0u, 0u, 0u, 0u, 0u, 0u)
    override fun onRecievedDataPacket(device: BluetoothDevice, heartRate: UIntArray) {
        this.heartRate = heartRate
    }
}