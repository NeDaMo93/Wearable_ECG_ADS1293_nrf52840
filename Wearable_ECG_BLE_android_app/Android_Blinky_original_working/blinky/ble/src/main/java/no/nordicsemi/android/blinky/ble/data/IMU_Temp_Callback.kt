package no.nordicsemi.android.blinky.ble.data

import android.bluetooth.BluetoothDevice
import android.util.Log
import no.nordicsemi.android.ble.callback.profile.ProfileReadResponse
import no.nordicsemi.android.ble.data.Data
import java.util.*

abstract class IMU_Temp_Callback: ProfileReadResponse() {

    override fun onDataReceived(device: BluetoothDevice, data: Data) {
        if (data.size() == 16) {
            val receivedValues = UIntArray(4)

            for (i in 0 until 4) {
                val startIndex = i * 4
                val receivedBytes = data.value?.copyOfRange(startIndex, startIndex + 4)
                receivedValues[i] = ((receivedBytes?.get(3)?.toUByte()?.toUInt() ?: 0u) shl 24) or
                        ((receivedBytes?.get(2)?.toUByte()?.toUInt() ?: 0u) shl 16) or
                        ((receivedBytes?.get(1)?.toUByte()?.toUInt() ?: 0u) shl 8) or
                        (receivedBytes?.get(0)?.toUByte()?.toUInt() ?: 0u)
            }

            onRecievedDataPacket(device, receivedValues)
        } else {
            onInvalidDataReceived(device, data)
        }
    }

    abstract fun onRecievedDataPacket(device: BluetoothDevice, IMU_temp_Data: UIntArray)
}
