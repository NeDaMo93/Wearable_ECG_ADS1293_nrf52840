package no.nordicsemi.android.blinky.ble.data

import android.bluetooth.BluetoothDevice

class HeartRate: HeartRateCallback() {
    var heartRate: IntArray = intArrayOf(0, 0, 0, 0, 0, 0, 0)
    override fun onRecievedDataPacket(device: BluetoothDevice, heartRate: IntArray) {
        this.heartRate = heartRate
    }
}