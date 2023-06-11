package no.nordicsemi.android.blinky.control.repository

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import no.nordicsemi.android.blinky.spec.Blinky
import no.nordicsemi.android.log.LogContract
import no.nordicsemi.android.log.timber.nRFLoggerTree
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named
import java.util.*
/**
 *
 * @param context The application context.
 * @param deviceId The device ID.
 * @param deviceName The name of the Blinky device, as advertised.
 * @property blinky The Blinky implementation.
 */
class BlinkyRepository @Inject constructor(
    @ApplicationContext context: Context,
    @Named("deviceId") deviceId: String,
    @Named("deviceName") deviceName: String,
    private val blinky: Blinky,
): Blinky by blinky {
    /** Timber tree that logs to nRF Logger. */
    private val tree: Timber.Tree

    /** If the nRF Logger is installed, this URI will allow to open the session. */
    internal val sessionUri: Uri?

    init {
        // Plant a new Tree that logs to nRF Logger.
        tree = nRFLoggerTree(context, null, deviceId, deviceName)
            .also { Timber.plant(it) }
            .also { sessionUri = it.session?.sessionUri }
    }

    val loggedHeartRate: Flow<UIntArray>
        get() = blinky.heartRate.onEach {
                Timber.log(LogContract.Log.Level.APPLICATION, "NEW HEART RATE ")
        }

    val loggedtempIMU: Flow<UIntArray>
        get() = blinky.IMU_temp_Data.onEach {
            Timber.log(LogContract.Log.Level.APPLICATION, "NEW temp IMU value ")
        }

    val loggedtempPosition: Flow<UIntArray>
        get() = blinky.temp_Position_Data.onEach {
            Timber.log(LogContract.Log.Level.APPLICATION, "NEW temp Position value ")
        }
    override fun release() {
        Timber.uproot(tree)
        blinky.release()
    }
}
