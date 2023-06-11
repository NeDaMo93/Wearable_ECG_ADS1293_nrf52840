package no.nordicsemi.android.blinky.control.view

import com.github.mikephil.charting.data.Entry

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin


data class ApplyFFT (val x: Float, val y: Float)

data class Complex(val real: Float, val imag: Float) {
    operator fun plus(c: Complex) = Complex(real + c.real, imag + c.imag)
    operator fun minus(c: Complex) = Complex(real - c.real, imag - c.imag)
    operator fun times(c: Complex) = Complex(real * c.real - imag * c.imag, real * c.imag + imag * c.real)

    fun abs() = kotlin.math.sqrt((real * real + imag * imag).toDouble())

    companion object {
        fun polar(rho: Float, theta: Float) = Complex((rho * cos(theta.toDouble())).toFloat(), (rho * sin(theta.toDouble())).toFloat())
    }
}
// Function to apply FFT on Entry.y values
fun fft(entries: List<Entry>): List<Double> {
    val sampleRate = 100F  //3.125F
    val nyquistFrequency = sampleRate / 2
    val closestPowerOfTwo = (1..32).firstOrNull { it >= entries.size }?.let { 1 shl it } ?: entries.size
    val paddedEntries = entries + List(closestPowerOfTwo - entries.size) { Entry(0.0F, 0.0F) }

    val yValues = paddedEntries.map { it.y }

    // Apply the Hanning window to the input signal followed by FFT
    val yValuesWindowed = applyHanningWindow(yValues)

    val fftResult = fftRecursive(yValuesWindowed)

    // Only keep frequencies less than or equal to the Nyquist frequency
    val result = fftResult.take((nyquistFrequency * fftResult.size / sampleRate).toInt())

    return result.map { it.abs() }
}

fun fftRecursive(x: List<Float>): List<Complex> {
    val n = x.size
    if (n == 1) {
        return listOf(Complex(x[0], 0.0F))
    }

    val even = fftRecursive(x.filterIndexed { index, _ -> index % 2 == 0 })
    val odd = fftRecursive(x.filterIndexed { index, _ -> index % 2 != 0 })

    val combined = MutableList(n) { Complex(0.0F, 0.0F) }
    for (k in 0 until n / 2) {
        val t = Complex.polar(1.0F, (-2.0 * PI * k / n).toFloat()) * odd[k]
        combined[k] = even[k] + t
        combined[k + n / 2] = even[k] - t
    }
    return combined
}
// Function to remove the mean from a list of input values

fun removeMean(input: List<Float>): List<Float> {
    val mean = input.sum() / input.size
    return input.map { it - mean }
}
/*
fun removeMean(input: List<Entry>): List<Entry> {
    val mean = input.sumOf { it.y.toDouble() } / input.size
    return input.map { Entry(it.x, it.y - mean.toFloat()) }
}*/

// Function to apply the Hanning window to the input signal
fun applyHanningWindow(input: List<Float>): List<Float> {
    val n = input.size
    return input.mapIndexed { index, value ->
        value * (0.5F * (1.0F - cos(2.0F * PI.toFloat() * index / (n - 1))))
    }.map { it.toFloat() }
}

fun detectActivity(magnitudes: List<Double>, samplingRate: Float): String {
    val n = magnitudes.size
    val maxFrequencyIndex = magnitudes.indices.maxByOrNull { magnitudes[it] } ?: 5
    val dominantFrequency = maxFrequencyIndex * (samplingRate / n)
    println("dominantFrequency is: $dominantFrequency")

    return when {
        dominantFrequency < 1.5 -> "RESTING"
        dominantFrequency in 1.6..2.2 -> "WALKING"
        else -> "RUNNING"
    }
}
fun lowPassFilter(data: List<Float>, cutoffFrequency: Double, samplingRate: Int): List<Float> {
    val filterOrder = 101
    val filterCoefficients = generateLowPassFIRCoefficients(filterOrder, cutoffFrequency, samplingRate)

    return data.mapIndexed { index, _ ->
        var filteredValue = 0.0
        for (i in 0 until filterOrder) {
            val dataIndex = index - i
            if (dataIndex >= 0) {
                filteredValue += data[dataIndex] * filterCoefficients[i]
            }
        }
        filteredValue.toFloat()
    }
}
fun generateLowPassFIRCoefficients(filterOrder: Int, cutoffFrequency: Double, samplingRate: Int): List<Double> {
    val filterCoefficients = mutableListOf<Double>()
    val halfOrder = filterOrder / 2

    for (i in 0 until filterOrder) {
        val n = i - halfOrder
        val coefficient = if (n == 0) {
            2 * cutoffFrequency / samplingRate
        } else {
            sin(2 * PI * n * cutoffFrequency / samplingRate) / (PI * n)
        }
        filterCoefficients.add(coefficient)
    }

    // Normalize the coefficients
    val sum = filterCoefficients.sum()
    return filterCoefficients.map { it / sum }
}
fun createFrequencyDomainLowPassFilter(cutoffFrequency: Float, sampleRate: Float, fftSize: Int): List<Float> {
    val cutoffIndex = (cutoffFrequency * fftSize / sampleRate).toInt()

    return List(fftSize) { index ->
        if (index <= cutoffIndex || index >= (fftSize - cutoffIndex)) {
            1.0F
        } else {
            0.0F
        }
    }
}
fun applyFrequencyDomainLowPassFilter(fftResult: List<Complex>, lowPassFilter: List<Float>): List<Complex> {
    return fftResult.zip(lowPassFilter) { complex, filterValue ->
        Complex(complex.real * filterValue, complex.imag * filterValue)
    }
}