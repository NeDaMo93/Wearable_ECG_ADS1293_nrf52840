package no.nordicsemi.android.blinky.control.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import no.nordicsemi.android.blinky.control.repository.BlinkyRepository
import no.nordicsemi.android.common.logger.NordicLogger
import javax.inject.Inject
import javax.inject.Named

/**
 * The view model for the Blinky screen.
 *
 * @param context The application context.
 * @property repository The repository that will be used to interact with the device.
 * @property deviceName The name of the Blinky device, as advertised.
 */
@HiltViewModel
class BlinkyViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val repository: BlinkyRepository,
    @Named("deviceName") val deviceName: String,
) : AndroidViewModel(context as Application) {
    /** The connection state of the device. */
    val state = repository.state
    /** The ECG data. */
    val heartRate = repository.loggedHeartRate
        .stateIn(viewModelScope, SharingStarted.Lazily, uintArrayOf())
    /** The temperatur data. */
    val IMU_temp_Data = repository.loggedtempIMU
        .stateIn(viewModelScope, SharingStarted.Lazily, uintArrayOf())
    /** The temperatur & position data. */
    val temp_Position_Data = repository.loggedtempPosition
        .stateIn(viewModelScope, SharingStarted.Lazily, uintArrayOf())
    init {
        // In this sample we want to connect to the device as soon as the view model is created.
        connect()
    }

    /**
     * Connects to the device.
     */
    fun connect() {
        val exceptionHandler = CoroutineExceptionHandler { _, _ -> }
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            // This method may throw an exception if the connection fails,
            // Bluetooth is disabled, etc.
            // The exception will be caught by the exception handler and will be ignored.
            repository.connect()
        }
    }

    /**
     * Opens Logger app with the log or Google Play if the app is not installed.
     */
    fun openLogger() {
        NordicLogger.launch(getApplication(), repository.sessionUri)
    }

    override fun onCleared() {
        super.onCleared()
        repository.release()
    }
}