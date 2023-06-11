package no.nordicsemi.android.blinky.control.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import no.nordicsemi.android.common.theme.NordicTheme
import androidx.compose.runtime.remember
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay

@Composable
internal fun BlinkyControlView(
    ledState: Boolean,
    heartRate: IntArray,
    IMU_temp_Data: UIntArray,
    onStateChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        LedControlView(
            state = ledState,
            onStateChanged = onStateChanged,
        )
        HeartRateControlView(
            heartRate = heartRate
        )
        IMU_Temp_ControlView(
            IMU_temp_Data = IMU_temp_Data
        )

    }
}

@Preview
@Composable
private fun BlinkyControlViewPreview() {
    NordicTheme {
        BlinkyControlView(
            ledState = true,
            heartRate = intArrayOf(0,0,0,0,0,0,0),
            IMU_temp_Data =  uintArrayOf(0u,0u,0u,0u,0u,0u,0u),
            onStateChanged = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}