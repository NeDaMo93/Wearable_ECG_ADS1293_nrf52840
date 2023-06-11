package no.nordicsemi.android.blinky.control.view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.mikephil.charting.data.Entry
import no.nordicsemi.android.common.theme.NordicTheme


val temp_values  = temp_imu_Container()
val imu_values  = temp_imu_Container()
var transfert_rate = 0.0f
val sampling_rate = 100F
val resting_threshold = 11.2F
val walking_threshold = 12F
val running_threshold = 20F
val numPeaksThreshold = 10

val batchSize = 50


@Composable
fun RealtimePlotingLineChart(entries1: List<Entry>, entries2: List<Entry>,magnitudes: List<Double>, activityLevel: String, detectedActivity:String ) {

    val magnitudesWithFrequency = magnitudes.mapIndexed { index, magnitude ->
        val frequency = index * (sampling_rate / magnitudes.size)
        Entry(frequency.toFloat(), magnitude.toFloat())
    }

    // Combine activityLevel and activity strings
    val combinedText = "$activityLevel, $detectedActivity"
    val plotModeState = remember { mutableStateOf("Time") }

    var maxEntry = when (plotModeState.value) {
        "Time" -> maxOf(entries1.maxByOrNull { it.y }?.y ?: 0f, entries1.maxByOrNull { it.y }?.y ?: 0f)
        "Frequency" -> (magnitudes.maxOrNull() ?: 0.0).toFloat()
        else -> 0f
    }
    var minEntry = when (plotModeState.value) {
        "Time" -> minOf(entries1.minByOrNull { it.y }?.y ?: 0f, entries1.minByOrNull { it.y }?.y ?: 0f)
        "Frequency" -> (magnitudes.minOrNull() ?: 0.0).toFloat()
        else -> 0f
    }

    if (maxEntry < resting_threshold) {
        maxEntry = 20F
        minEntry = 0F
    }
    val minTime = when (plotModeState.value) {
        "Time" -> minOf(entries1.minByOrNull { it.x }?.x ?: 0f, entries1.minByOrNull { it.x }?.x ?: 0f)
        "Frequency" -> 0f
        else -> 0f
    }
    val maxTime = when (plotModeState.value) {
        "Time" -> maxOf(entries1.maxByOrNull { it.x }?.x ?: 0f)
        "Frequency" -> {
            val frequencyResolution = sampling_rate / magnitudes.size
            ((magnitudes.size - 1) * frequencyResolution).toFloat()
        }
        else -> 0f
    }
    val xAxisRange = maxTime - minTime
    val effectiveXAxisEnd = maxTime ?: maxOf(entries1.maxByOrNull { it.x }?.x ?: 0f, entries2.maxByOrNull { it.x }?.x ?: 0f)

    var scale by remember { mutableStateOf(1f) }

    Column(
        modifier = Modifier.fillMaxHeight(),
    ) {
        Button(
            onClick = {
                if (plotModeState.value == "Time") {
                    plotModeState.value = "Frequency"
                } else {
                    plotModeState.value = "Time"
                }
            },
            modifier = Modifier
                .height(40.dp)
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 8.dp)
        ) {
            Text("Switch to ${if (plotModeState.value == "Time") "Frequency" else "Time"} Domain")
        }
        // Add legend above the Canvas
        Row(
            horizontalArrangement = Arrangement.Center
        ) {
            // Display the latest value of the second entry
            val legendTitles = when (plotModeState.value) {
                "Time" -> listOf("Activity")
                "Frequency" -> {
                    listOf("Magnitude")
                }
                else -> listOf<String>()
            }

            val legendColors =
                listOf(Color.Green)

            legendTitles.forEachIndexed { index, title ->
                Row(
                    modifier = Modifier.padding(end = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .background(legendColors[index])
                    )
                    Text(
                        text = title,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(190.dp) // Adjust the height here
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
        val verticalMargin = 60f
        val xAxisTitle = if (plotModeState.value == "Time") "Time (s)" else "Frequency (Hz)"
        //val xAxisTitle = "Time (s)" // Replace this with the desired title
        val xAxisTitleX = (canvasWidth - horizontalMargin) / 2
        val xAxisTitleY = canvasHeight - verticalMargin + 40 // Adjust the value to position the title below the scale marks

        with(drawContext.canvas.nativeCanvas) {

            // Display the activity level string
            val activityLevelX = (canvasWidth / 2) - 40
            val activityLevelY = verticalMargin - 110
            drawText(
                combinedText,
                activityLevelX,
                activityLevelY,
                android.graphics.Paint().apply {
                    textSize = 50f
                    color = Color.Black.toArgb()
                    textAlign = android.graphics.Paint.Align.LEFT
                }
            )
        }
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
            if (!(i == 0 && minTime > 0)) {
                drawContext.canvas.nativeCanvas.drawText(
                    "%.1f".format(minTime + i / 10f * xAxisRange),
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

        // Draw entry points and line charts
        if (entries1.size >= 2) {
            val path = Path()
            var lastPoint: Offset? = null
            val entriesToPlot = when (plotModeState.value) {
                "Time" -> entries1
                "Frequency" -> {
                    // Replace this part with magnitudesWithFrequency
                    magnitudesWithFrequency
                }
                else -> listOf()
            }
            entriesToPlot.forEachIndexed { index, entry ->
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
                color = Color.Green,
                path = path,
                style = Stroke(5f),
                //cap = StrokeCap.Round
            )
        }
    }

}

var previousActivity = ""
var activityLevel = "Checking activity ..." // Initialize activityLevel variable
val previousMagnitudes = mutableListOf<Double>()
@Composable
internal fun IMU_Temp_ControlView(
    IMU_temp_Data: UIntArray,
    modifier: Modifier = Modifier,
) {


    if (IMU_temp_Data.size >= 4) { //buffer contains 4 values
        val imu_values_temp = IMU_temp_Data.copyOfRange(0, 3)
        val temp_values_temp = uintArrayOf(IMU_temp_Data[IMU_temp_Data.size - 1])

        imu_values.add_imu_temp_Rate(imu_values_temp)
        temp_values.add_imu_temp_Rate(temp_values_temp)
    }

    var temp_list = temp_values.getVal2Plot()
    var imu_list = imu_values.getVal2Plot()

    temp_list = temp_list.map { entry -> Entry(entry.x, ((entry.y/ 1000000f) * 2) - 0.25F)}
    imu_list = imu_list.map { entry -> Entry(entry.x, ((entry.y/ 1000000f) * 2) - 0.25F) }

    //if (imu_list.size > 10){
    //imu_list = imu_values.applyAverageFilter(imu_list, 10)}

    // Collect imu_list data and check activity level when it reaches 50 entries
    val yValues = mutableListOf<Float>()
    var copy_of_yValues = mutableListOf<Float>()
    val walkingpeakIndices = mutableListOf<Int>()
    val restingpeakIndices = mutableListOf<Int>()
    val runningpeakIndices = mutableListOf<Int>()
    val averages = mutableListOf<Float>()
    var walking_numPeaks = 0
    var resting_numPeaks = 0
    var running_numPeaks = 0

    var numEntries = 0
    var magnitudes = listOf<Double>()

    for (entry in imu_list) {
        // Collect y-values in a list
        if (entry.y > 2) {
            yValues.add(entry.y)
        }
        numEntries++
    }
    if (yValues.size > batchSize )
    {
        magnitudes = fft(imu_list)
        magnitudes = magnitudes.drop(1)  // drop first index
        if (magnitudes.isNotEmpty()) {
            previousMagnitudes.clear()
            previousMagnitudes.addAll(magnitudes)
        }
        copy_of_yValues = yValues.subList(yValues.size - batchSize, yValues.size)
    }

    val min = copy_of_yValues.minOrNull() ?: 0f // Minimum; if the list is empty, use 0
    val max = copy_of_yValues.maxOrNull() ?: 0f // Maximum; if the list is empty, use 0

    val median = (max + min) / 2

    for (index in copy_of_yValues.indices)  {

        val value = copy_of_yValues[index]
        if (value >= median) {
            if (value < resting_threshold ) {
                restingpeakIndices.add(index)
                resting_numPeaks++
            }
            if (value > walking_threshold && value < running_threshold) {
                walkingpeakIndices.add(index)
                walking_numPeaks++
            }
            else if (value > running_threshold)
            {
                runningpeakIndices.add(index)
                running_numPeaks++
            }
        }

    }

    //change this to be called each 4 s
    // Check activity level for the collected data

    if (copy_of_yValues.size >= batchSize) {

        val numPeaksAbove_resting_Threshold = restingpeakIndices.size
        val numPeaksAbove_walking_Threshold = walkingpeakIndices.size
        val numPeaksAbove_running_Threshold = runningpeakIndices.size

        if (numPeaksAbove_resting_Threshold >= numPeaksThreshold)
        {
            activityLevel =  "Low activity"
            previousActivity = "RESTING"
        }
        else if (numPeaksAbove_walking_Threshold >= numPeaksThreshold)
        {
            activityLevel =  "Medium activity"
            previousActivity = "Walking"
        }
        else if (numPeaksAbove_running_Threshold >= numPeaksThreshold)
        {
            activityLevel =  "High activity"
            previousActivity = "Running"
        }

        averages.clear()
        restingpeakIndices.clear()
        walkingpeakIndices.clear();
        runningpeakIndices.clear();

    }

    RealtimePlotingLineChart(entries1 = imu_list, entries2 = temp_list, magnitudes = previousMagnitudes, activityLevel = activityLevel, detectedActivity = previousActivity)

}


@Composable
@Preview
private fun IMU_temp_ControlViewPreview() {
    NordicTheme {
        IMU_Temp_ControlView(
            IMU_temp_Data = uintArrayOf(0u, 0u, 0u, 0u, 0u, 0u, 0u, 0u),
            modifier = Modifier.padding(16.dp),
        )
    }
}

typealias FloatDataPair = Pair<Float, Float>
class temp_imu_Container {
    private final val MAX_ARRAY_SIZE = 400
    private final val rxFreq = 0.01f // packet with 4 values is sent each 0.04 sec
    private final val VALUES_TO_PLOT = 200

    private val temp_imu_values = mutableListOf<FloatPair>()

    fun getVal2Plot(): List<Entry>
    {
        var lastPair2plot : MutableList<FloatPair> = mutableListOf()
        var list2plotConverted = listOf(Entry(0f,0f))

        if (temp_imu_values.size > VALUES_TO_PLOT){
            lastPair2plot = temp_imu_values.subList(temp_imu_values.size - VALUES_TO_PLOT, temp_imu_values.size);

        }
        else{
            lastPair2plot = temp_imu_values
        }

        list2plotConverted = convertToEntryList(lastPair2plot)
        return list2plotConverted
    }

    fun convertToEntryList(ConvertlastPair2plot:MutableList<FloatDataPair> = mutableListOf()): List<Entry> {
        return ConvertlastPair2plot.map { Entry(it.second, it.first) }.toList()
    }

    fun add_imu_temp_Rate(IMU_temp_Data: UIntArray) {
        for (imu_temp in IMU_temp_Data) {
            val newPair = Pair(imu_temp.toFloat(), transfert_rate)
            temp_imu_values.add(newPair)
            transfert_rate += rxFreq

            if (temp_imu_values.size > MAX_ARRAY_SIZE) {
                // remove the oldest value
                temp_imu_values.removeFirst()
            }
        }
    }

    fun applyAverageFilter(imuList: List<Entry>, filterSize: Int): List<Entry> {
        val filteredEntries = mutableListOf<Entry>()

        for (i in imuList.indices) {
            var sum = 0f
            var count = 0
            for (j in i - filterSize / 2 until i + filterSize / 2 + 1) {
                if (j >= 0 && j < imuList.size) {
                    sum += imuList[j].y
                    count++
                }
            }
            val filteredY = sum / count
            filteredEntries.add(Entry(imuList[i].x, filteredY))
        }

        return filteredEntries
    }

    fun calculateSamplingRate(entries: List<Entry>): Float {
        if (entries.size < 2) {
            throw IllegalArgumentException("At least two entries are required to calculate the sampling rate.")
        }

        var totalTime = 0.0
        for (i in 1 until entries.size) {
            totalTime += (entries[i].x - entries[i - 1].x)
        }

        val averageTimeBetweenSamples = totalTime / (entries.size - 1)
        return (1.0 / averageTimeBetweenSamples).toFloat()
    }

}



