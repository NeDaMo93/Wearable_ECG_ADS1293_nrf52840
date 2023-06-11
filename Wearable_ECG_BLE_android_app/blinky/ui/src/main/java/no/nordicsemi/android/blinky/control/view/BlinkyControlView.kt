package no.nordicsemi.android.blinky.control.view

import android.util.Log
import androidx.compose.foundation.layout.*
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
    heartRate: UIntArray,
    IMU_temp_Data: UIntArray,
    temperature_position:UIntArray,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {

        HeartRateControlView(
            heartRate = heartRate,
            temperature_position = temperature_position,
        )
        IMU_Temp_ControlView(
            IMU_temp_Data = IMU_temp_Data,
        )
    }
}

@Preview
@Composable
private fun BlinkyControlViewPreview() {
    NordicTheme {
        BlinkyControlView(
            heartRate = uintArrayOf(0u,0u,0u,0u,0u,0u,0u),
            IMU_temp_Data =  uintArrayOf(0u,0u,0u,0u,0u,0u,0u),
            temperature_position =  uintArrayOf(0u,0u),
            modifier = Modifier.padding(16.dp),
        )
    }
}