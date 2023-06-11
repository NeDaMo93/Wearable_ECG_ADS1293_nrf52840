package no.nordicsemi.android.blinky.control.view

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import no.nordicsemi.android.blinky.control.R
import no.nordicsemi.android.common.theme.NordicTheme
import java.util.*
import kotlin.math.min
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlinx.coroutines.delay
import kotlin.random.Random
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.text.SimpleDateFormat
import kotlin.math.max


val ecg_channel_1  = HeartRateContainer()
val ecg_channel_2  = HeartRateContainer()
var elapsedTime = 0.0f

@Composable
fun LineChart(entries: List<Entry>, colortoDraw: Color, legend:String, temp:UInt, position:String) {

    val temp_val_to_plot = temp.toFloat() / 100
    val maxEntry = maxOf(entries.maxByOrNull { it.y }?.y ?: 0f, entries.maxByOrNull { it.y }?.y ?: 0f)
    val minEntry = minOf(entries.minByOrNull { it.y }?.y ?: 0f, entries.minByOrNull { it.y }?.y ?: 0f)

    val maxTime = maxOf(entries.maxByOrNull { it.x }?.x ?: 0f, entries.maxByOrNull { it.x }?.x ?: 0f)
    val minTime = minOf(entries.minByOrNull { it.x }?.x ?: 0f, entries.minByOrNull { it.x }?.x ?: 0f)

    val effectiveXAxisEnd = maxTime
    val xAxisRange = effectiveXAxisEnd - minTime

    var scale by remember { mutableStateOf(1f) }

    Column {
        if (legend == "ECG channel 1") {
            // Box for the heart rate
            Box(
                modifier = Modifier
                    .width(600.dp)
                    .padding(0.dp)
                    .border(2.dp, Color.Gray)
                    .padding(7.dp)
            ) {
                Text(
                    text = "Body Temperature: $temp_val_to_plot Celsius",
                    color = Color.Blue,
                    fontSize = 14.sp
                )
            }

            Column(
                modifier = Modifier.fillMaxHeight(),
            ) {
                Box(
                    modifier = Modifier
                        .width(600.dp)
                        .padding(0.dp)
                        .border(2.dp, Color.Gray)
                        .padding(7.dp)
                ) {
                    Text(
                        text = "Body position: $position ",
                        color = Color.Magenta,
                        fontSize = 14.sp
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(1.dp)) // Adjust the height to change the amount of space.

        // Add legend above the Canvas
        Row(
            modifier = Modifier.padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            val legendColor = colortoDraw
            Row(
                modifier = Modifier.padding(end = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(legendColor)
                )
                Text(
                    text = legend,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

        }
    }


    Canvas(
        modifier = Modifier
            .size(width = 900.dp, height = 130.dp)
            .transformable(
                // Pass in the scale state and update it when zooming
                state = rememberTransformableState { zoomChange, _, _ ->
                    scale *= zoomChange
                }
            )
    )  {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val horizontalMargin = 50f
        val verticalMargin = 80f
        val xAxisTitle = "Time (s)" // Replace this with the desired title
        val xAxisTitleX = (canvasWidth - horizontalMargin) / 2
        val xAxisTitleY = canvasHeight - verticalMargin + 35

        val yRange = maxEntry

        // Draw x-axis
        drawLine(
            color = Color.Black,
            start = Offset(horizontalMargin, canvasHeight - verticalMargin),
            end = Offset(canvasWidth - horizontalMargin, canvasHeight - verticalMargin),
            strokeWidth = 2f
        )

        // Draw x-axis scale
        for (i in 0..10) {
            val x = horizontalMargin + i / 10f * (canvasWidth - 2 * horizontalMargin)
            val y = canvasHeight - verticalMargin + 10
            drawLine(
                color = Color.Black,
                start = Offset(x, canvasHeight - verticalMargin),
                end = Offset(x, y),
                strokeWidth = 2f
            )
            if (!(i == 0 && minTime > 0)) { // Add this condition
                drawContext.canvas.nativeCanvas.drawText(
                    "%.1f".format(minTime + i / 110f * xAxisRange),
                    x,
                    y + 10,
                    android.graphics.Paint().apply {
                        textSize = 20f
                        color = Color.Black.toArgb()
                        textAlign = android.graphics.Paint.Align.CENTER
                    }
                )
            }
        }

        drawContext.canvas.nativeCanvas.drawText(
            xAxisTitle,
            xAxisTitleX,
            xAxisTitleY,
            android.graphics.Paint().apply {
                textSize = 24f
                color = Color.Black.toArgb()
                textAlign = android.graphics.Paint.Align.CENTER
            }
        )
        // Draw y-axis
        drawLine(
            color = Color.Black,
            start = Offset(horizontalMargin, verticalMargin),
            end = Offset(horizontalMargin, canvasHeight - verticalMargin),
            strokeWidth = 2f
        )
        // Draw y-axis scale
        for (i in 0..10) {
            val x = 10f
            val y = canvasHeight - verticalMargin - i / 10f * (canvasHeight - 2 * verticalMargin)
            drawLine(
                color = Color.Black,
                start = Offset(horizontalMargin, y),
                end = Offset(horizontalMargin - 10, y),
                strokeWidth = 2f
            )
            if (!(i == 0 && minEntry > 0)) {
                drawContext.canvas.nativeCanvas.drawText(
                    "%.1f".format(minEntry + i / 10f * (maxEntry - minEntry)),
                    x,
                    y,
                    android.graphics.Paint().apply {
                        textSize = 20f
                        color = Color.Black.toArgb()
                        textAlign = android.graphics.Paint.Align.CENTER
                    }
                )
            }
        }
        // Draw entry points and line charts
        if (entries.size >= 2) {
            val path = Path()
            var lastPoint: Offset? = null
            entries.forEachIndexed { index, entry ->
                if (entry.x >= minTime && entry.x <= effectiveXAxisEnd) {
                    val pointX = horizontalMargin + (entry.x - minTime) / xAxisRange * (canvasWidth - 2 * horizontalMargin)
                    val pointY = canvasHeight - verticalMargin - (entry.y - minEntry) / (maxEntry - minEntry) * (canvasHeight - 2 * verticalMargin)
                    val currentPoint = Offset(pointX, pointY)

                    // Draw line chart
                    if (lastPoint != null) {
                        path.lineTo(currentPoint.x, currentPoint.y)
                    } else {
                        path.moveTo(currentPoint.x, currentPoint.y)
                    }
                    lastPoint = currentPoint
                }
            }
            drawPath(
                color = colortoDraw,
                path = path,
                style = Stroke(5f),
            )
        }
    }
}

@Composable
fun RealtimeLineChart(entries1: List<Entry>, entries2: List<Entry>, temp:UInt, position:String) {
    Column {
        LineChart(entries1, Color.Red, "ECG channel 1", temp, position)
        LineChart(entries2, Color.DarkGray, "ECG channel 2", 0u, "0u")
    }
}

@Composable
internal fun HeartRateControlView(
    heartRate: UIntArray,
    temperature_position:UIntArray,
    modifier: Modifier = Modifier,
) {
    var determinated_body_position = "Detecting body position"
    var temperature = 0u
    // separate channel 1 and channel 2 data
    val ecg_channel1_Array = heartRate.filterIndexed { index, _ -> index % 2 == 0 }.toUIntArray()
    val ecg_channel2_Array = heartRate.filterIndexed { index, _ -> index % 2 != 0 }.toUIntArray()
    //read out the temeprature_position data
    if (temperature_position.size >= 2) {
        temperature = temperature_position[0]
        val body_position = temperature_position[1]

        determinated_body_position = if (body_position == 1u) {
            "Up right"
        } else if (body_position == 2u) {
            "Handstand"
        } else if (body_position == 3u) {
            "Lying sideways left side"
        } else if (body_position == 4u) {
            "Lying sideways right side"
        } else if (body_position == 5u) {
            "Lying on Back"
        } else if (body_position == 6u) {
            "Lying on Stomach"
        } else {
            "Detecting body position..."
        }

        // print the temperature and body_position values
        println("Temperature: $temperature")
        println("Body Position: $body_position")
    }

    ecg_channel_1.addHeartRate(ecg_channel1_Array)
    ecg_channel_2.addHeartRate(ecg_channel2_Array)

    //here extract 2 channels
    var ecg_list_channel1 = ecg_channel_1.getVal2Plot()
    var ecg_list_channel2 = ecg_channel_2.getVal2Plot()

    RealtimeLineChart(entries1 = ecg_list_channel1, entries2 = ecg_list_channel2, temp = temperature, position = determinated_body_position)
}


@Composable
@Preview
private fun HeartRateControlViewPreview() {
    NordicTheme {
        HeartRateControlView(
            heartRate = uintArrayOf(0u,0u,0u,0u,0u,0u,0u),
            temperature_position = uintArrayOf(0u,0u),
            modifier = Modifier.padding(16.dp),
        )

    }
}

typealias FloatPair = Pair<Float, Float>
class HeartRateContainer {
    private final val MAX_ARRAY_SIZE = 400
    private final val rxFreq = 0.01f //each 0.01 s a value
    private final val VALUES_TO_PLOT = 250

    private val heartRates = mutableListOf<FloatPair>()

    fun getVal2Plot(): List<Entry>
    {
        var lastPair2plot : MutableList<FloatPair> = mutableListOf()
        var list2plotConverted = listOf(Entry(0f,0f))

        if (heartRates.size > VALUES_TO_PLOT){
            lastPair2plot = heartRates.subList(heartRates.size - VALUES_TO_PLOT, heartRates.size);

        }
        else{
            lastPair2plot = heartRates
        }


        list2plotConverted = convertToEntryList(lastPair2plot)
        return list2plotConverted
    }

    fun convertToEntryList(ConvertlastPair2plot:MutableList<FloatPair> = mutableListOf()): List<Entry> {
        return ConvertlastPair2plot.map { Entry(it.second, it.first) }.toList()
    }


    fun addHeartRate(heartRate: UIntArray) {
        for (hr in heartRate) {
            val newPair = Pair(hr.toFloat(), elapsedTime)
            heartRates.add(newPair)
            elapsedTime += rxFreq

            if (heartRates.size > MAX_ARRAY_SIZE) {
                // remove the oldest value
                heartRates.removeFirst()
            }

            heartRates.forEach { floatPair ->
                println(floatPair.first) // or println(floatPair.second) for the second value
            }
        }
    }


}

