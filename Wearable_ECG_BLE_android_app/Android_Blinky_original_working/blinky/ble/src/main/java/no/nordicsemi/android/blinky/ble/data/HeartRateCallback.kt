package no.nordicsemi.android.blinky.ble.data

import android.bluetooth.BluetoothDevice
import android.util.Log
import no.nordicsemi.android.ble.callback.profile.ProfileReadResponse
import no.nordicsemi.android.ble.data.Data
import java.util.*

abstract class HeartRateCallback: ProfileReadResponse() {

    override fun onDataReceived(device: BluetoothDevice, data: Data) {

        if (data.size() > 1) {
            val receivedValues = IntArray(data.size())

            for (i in 0 until data.size()) {
                receivedValues[i] = (data.value?.get(i)?.toInt() ?: 0) and 0xFF
            }
            onRecievedDataPacket(device, receivedValues)
        } else {
            onInvalidDataReceived(device, data)
        }
    }

    abstract fun onRecievedDataPacket(device: BluetoothDevice, heartRate: IntArray)
}
