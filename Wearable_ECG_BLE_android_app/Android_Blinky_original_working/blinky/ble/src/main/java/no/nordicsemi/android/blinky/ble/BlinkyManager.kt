package no.nordicsemi.android.blinky.ble

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.content.Context
import android.util.Log
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import no.nordicsemi.android.ble.BleManager
import no.nordicsemi.android.ble.ktx.asValidResponseFlow
import no.nordicsemi.android.ble.ktx.getCharacteristic
import no.nordicsemi.android.ble.ktx.state.ConnectionState
import no.nordicsemi.android.ble.ktx.stateAsFlow
import no.nordicsemi.android.ble.ktx.suspend
import no.nordicsemi.android.blinky.ble.data.HeartRateCallback
import no.nordicsemi.android.blinky.ble.data.HeartRate
import no.nordicsemi.android.blinky.ble.data.IMU_Temp_Callback
import no.nordicsemi.android.blinky.ble.data.IMU_temp_Data
import no.nordicsemi.android.blinky.ble.data.LedCallback
import no.nordicsemi.android.blinky.ble.data.LedData
import no.nordicsemi.android.blinky.spec.Blinky
import no.nordicsemi.android.blinky.spec.BlinkySpec
import timber.log.Timber

class BlinkyManager(
    context: Context,
    device: BluetoothDevice
): Blinky by BlinkyManagerImpl(context, device)

private class BlinkyManagerImpl(
    context: Context,
    private val device: BluetoothDevice,
): BleManager(context), Blinky {
    private val scope = CoroutineScope(Dispatchers.IO)

    private var ledCharacteristic: BluetoothGattCharacteristic? = null
    private var heartRateCharacteristic: BluetoothGattCharacteristic? = null
    private var temp_IMU_Characteristic: BluetoothGattCharacteristic? = null

    private val _ledState = MutableStateFlow(false)
    override val ledState = _ledState.asStateFlow()

    private val _heartRate = MutableStateFlow(intArrayOf())
    override val heartRate = _heartRate.asStateFlow()

    private val _IMU_Temp_Data = MutableStateFlow(uintArrayOf())
    override val IMU_temp_Data = _IMU_Temp_Data.asStateFlow()


    override val state = stateAsFlow()
        .map {
            when (it) {
                is ConnectionState.Connecting,
                is ConnectionState.Initializing -> Blinky.State.LOADING
                is ConnectionState.Ready -> Blinky.State.READY
                is ConnectionState.Disconnecting,
                is ConnectionState.Disconnected -> Blinky.State.NOT_AVAILABLE
            }
        }
        .stateIn(scope, SharingStarted.Lazily, Blinky.State.NOT_AVAILABLE)

    override suspend fun connect() = connect(device)
        .retry(3, 300)
        .useAutoConnect(false)
        .timeout(3000)
        .suspend()

    override fun release() {
        // Cancel all coroutines.
        scope.cancel()

        val wasConnected = isReady
        // If the device wasn't connected, it means that ConnectRequest was still pending.
        // Cancelling queue will initiate disconnecting automatically.
        cancelQueue()

        // If the device was connected, we have to disconnect manually.
        if (wasConnected) {
            disconnect().enqueue()
        }
    }

    override suspend fun turnLed(state: Boolean) {
        // Write the value to the characteristic.
        writeCharacteristic(
            ledCharacteristic,
            LedData.from(state),
            BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT
        ).suspend()

        // Update the state flow with the new value.
        _ledState.value = state
    }

    override fun log(priority: Int, message: String) {
        Timber.log(priority, message)
    }

    override fun getMinLogPriority(): Int {
        // By default, the library logs only INFO or
        // higher priority messages. You may change it here.
        return Log.VERBOSE
    }

    private val heartRateCallback by lazy {
        object : HeartRateCallback() {
            override fun onRecievedDataPacket(device: BluetoothDevice, heartRate: IntArray) {
                _heartRate.tryEmit(heartRate)
            }
        }
    }

    private val IMU_Temp_Callback by lazy {
        object : IMU_Temp_Callback() {
            override fun onRecievedDataPacket(device: BluetoothDevice, IMU_temp_Data: UIntArray) {
                _IMU_Temp_Data.tryEmit(IMU_temp_Data)
            }
        }
    }

    private val ledCallback by lazy {
        object : LedCallback() {
            override fun onLedStateChanged(device: BluetoothDevice, state: Boolean) {
                _ledState.tryEmit(state)
            }
        }
    }

    override fun isRequiredServiceSupported(gatt: BluetoothGatt): Boolean {
        // Get the LBS Service from the gatt object.
        gatt.getService(BlinkySpec.BLINKY_SERVICE_UUID)?.apply {
            // Get the LED characteristic.
            ledCharacteristic = getCharacteristic(
                BlinkySpec.BLINKY_LED_CHARACTERISTIC_UUID,
                // Mind, that below we pass required properties.
                // If your implementation supports only WRITE_NO_RESPONSE,
                // change the property to BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE.
                BluetoothGattCharacteristic.PROPERTY_WRITE
            )
            // Get the heart rate characteristic.
            heartRateCharacteristic = getCharacteristic(
                BlinkySpec.BLINKY_HEART_RATE_CHARACTERISTIC_UUID,
                BluetoothGattCharacteristic.PROPERTY_NOTIFY
            )

            // Get the temperature & IMU characteristic.
            temp_IMU_Characteristic = getCharacteristic(
                BlinkySpec.BLINKY_Temp_IMU_CHARACTERISTIC_UUID,
                BluetoothGattCharacteristic.PROPERTY_NOTIFY
            )

            // Return true if all required characteristics are supported.
            return ledCharacteristic != null && heartRateCharacteristic != null && temp_IMU_Characteristic != null
        }
        return false
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun initialize() {
        // Enable notifications for the heart rate characteristic.
        val flow: Flow<HeartRate> = setNotificationCallback(heartRateCharacteristic)
            .asValidResponseFlow()

        // Forward the new heart rate value to the heart rate flow.
        scope.launch {
            flow.map { it.heartRate }.collect { _heartRate.tryEmit(it) }
        }

        enableNotifications(heartRateCharacteristic)
            .enqueue()

        // Read the initial value of the heart rate characteristic.
        readCharacteristic(heartRateCharacteristic)
            .with(heartRateCallback)
            .enqueue()


        // Enable notifications for the IMU & temperature characteristic.
        val flow_f: Flow<IMU_temp_Data> = setNotificationCallback(temp_IMU_Characteristic)
            .asValidResponseFlow()

        scope.launch {
            flow_f.map { it.IMU_temp_Data }.collect { _IMU_Temp_Data.tryEmit(it) }
        }

        enableNotifications(temp_IMU_Characteristic)
            .enqueue()

        // Read the initial value of the heart rate characteristic.
        readCharacteristic(temp_IMU_Characteristic)
            .with(IMU_Temp_Callback)
            .enqueue()

        // Read the initial value of the LED characteristic.
        readCharacteristic(ledCharacteristic)
            .with(ledCallback)
            .enqueue()
    }

    override fun onServicesInvalidated() {
        ledCharacteristic = null
        heartRateCharacteristic = null
        temp_IMU_Characteristic = null
    }

}