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
import no.nordicsemi.android.blinky.ble.data.Temperature_Position_Callback
import no.nordicsemi.android.blinky.ble.data.temp_Position_Data
import no.nordicsemi.android.blinky.ble.data.HeartRate
import no.nordicsemi.android.blinky.ble.data.IMU_Temp_Callback
import no.nordicsemi.android.blinky.ble.data.IMU_temp_Data
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

    private var heartRateCharacteristic: BluetoothGattCharacteristic? = null
    private var temp_IMU_Characteristic: BluetoothGattCharacteristic? = null
    private var temp_body_position_Characteristic: BluetoothGattCharacteristic? = null

    private val _heartRate = MutableStateFlow(uintArrayOf())
    override val heartRate = _heartRate.asStateFlow()

    private val _IMU_Temp_Data = MutableStateFlow(uintArrayOf())
    override val IMU_temp_Data = _IMU_Temp_Data.asStateFlow()

    private val _Temp_Position_Data = MutableStateFlow(uintArrayOf())
    override val temp_Position_Data = _Temp_Position_Data.asStateFlow()


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
            override fun onRecievedDataPacket(device: BluetoothDevice, heartRate: UIntArray) {
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

    private val IMU_Position_Callback by lazy {
        object : Temperature_Position_Callback() {
            override fun onRecievedDataPacket(device: BluetoothDevice, Temp_position_data: UIntArray) {
                _Temp_Position_Data.tryEmit(Temp_position_data)
            }
        }
    }

    override fun isRequiredServiceSupported(gatt: BluetoothGatt): Boolean {
        // Get the LBS Service from the gatt object.
        gatt.getService(BlinkySpec.BLINKY_SERVICE_UUID)?.apply {
            // Get the heart rate characteristic.
            heartRateCharacteristic = getCharacteristic(
                BlinkySpec.BLINKY_HEART_RATE_CHARACTERISTIC_UUID,
                BluetoothGattCharacteristic.PROPERTY_NOTIFY
            )

            // Get IMU characteristic.
            temp_IMU_Characteristic = getCharacteristic(
                BlinkySpec.BLINKY_Temp_IMU_CHARACTERISTIC_UUID,
                BluetoothGattCharacteristic.PROPERTY_NOTIFY
            )

            // Get the temperature & body position characteristic.
            temp_body_position_Characteristic = getCharacteristic(
                BlinkySpec.BLINKY_TEMP_POSITION_CHARACTERISTIC_UUID,
                BluetoothGattCharacteristic.PROPERTY_NOTIFY
            )

            // Return true if all required characteristics are supported.
            return  heartRateCharacteristic != null && temp_IMU_Characteristic != null &&  temp_body_position_Characteristic != null
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


        // Enable notifications for the IMU characteristic.
        val flow_f: Flow<IMU_temp_Data> = setNotificationCallback(temp_IMU_Characteristic)
            .asValidResponseFlow()

        scope.launch {
            flow_f.map { it.IMU_temp_Data }.collect { _IMU_Temp_Data.tryEmit(it) }
        }

        enableNotifications(temp_IMU_Characteristic)
            .enqueue()

        // Read the initial value of imu rate characteristic.
        readCharacteristic(temp_IMU_Characteristic)
            .with(IMU_Temp_Callback)
            .enqueue()

        // Enable notifications for the temperature, body position characteristic.
        val flow_g: Flow<temp_Position_Data> = setNotificationCallback(temp_body_position_Characteristic)
            .asValidResponseFlow()

        scope.launch {
            flow_g.map { it.temp_Position_Data}.collect { _Temp_Position_Data.tryEmit(it) }
        }

        enableNotifications(temp_body_position_Characteristic)
            .enqueue()

        // Read the initial value of the temperature/position rate characteristic.
        readCharacteristic(temp_body_position_Characteristic)
            .with(IMU_Position_Callback)
            .enqueue()

    }

    override fun onServicesInvalidated() {
        heartRateCharacteristic = null
        temp_IMU_Characteristic = null
        temp_body_position_Characteristic = null
    }

}