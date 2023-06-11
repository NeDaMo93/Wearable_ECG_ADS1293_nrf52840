package no.nordicsemi.android.blinky.spec

import java.util.UUID

class BlinkySpec {

    companion object {
        val BLINKY_SERVICE_UUID: UUID = UUID.fromString("00001523-1212-efde-1523-785feabcd123")
        val BLINKY_HEART_RATE_CHARACTERISTIC_UUID: UUID = UUID.fromString("00001524-1212-efde-1523-785feabcd123")
        val BLINKY_Temp_IMU_CHARACTERISTIC_UUID: UUID = UUID.fromString("00001525-1212-efde-1523-785feabcd123")
        val BLINKY_TEMP_POSITION_CHARACTERISTIC_UUID: UUID = UUID.fromString("00001526-1212-efde-1523-785feabcd123")
    }

}