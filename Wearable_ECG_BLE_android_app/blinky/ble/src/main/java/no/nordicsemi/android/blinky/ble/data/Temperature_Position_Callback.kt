package no.nordicsemi.android.blinky.ble.data

import android.bluetooth.BluetoothDevice
import android.util.Log
import no.nordicsemi.android.ble.callback.profile.ProfileReadResponse
import no.nordicsemi.android.ble.data.Data
import java.util.*

abstract class Temperature_Position_Callback: ProfileReadResponse() {

    override fun onDataReceived(device: BluetoothDevice, data: Data) {
        if (data.size() == 4) {
            val receivedValues = UIntArray(2)

            for (i in 0 until 2) {
                val startIndex = i * 2
                val receivedBytes = data.value?.copyOfRange(startIndex, startIndex + 2)
                receivedValues[i] = ((receivedBytes?.get(1)?.toUByte()?.toUInt() ?: 0u) shl 8) or
                        (receivedBytes?.get(0)?.toUByte()?.toUInt() ?: 0u)
            }
            onRecievedDataPacket(device, receivedValues)

        } else {
            onInvalidDataReceived(device, data)
        }
    }
    abstract fun onRecievedDataPacket(device: BluetoothDevice, temp_Position_Data: UIntArray)
}
