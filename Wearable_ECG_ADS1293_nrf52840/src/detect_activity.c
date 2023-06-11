#include "detect_activity.h"
#include<stdio.h>
#include<string.h>
#include <stdlib.h>
#include <math.h>
#include <complex.h>
#include <stddef.h>
#include <stdbool.h>

#define PI 3.14159265358979323846

void apply_low_pass_filter(const float *data_buffer, float* filtered_data_after_lowpass, size_t data_size, float cutoff_freq, float sampling_freq) 
{
    // Calculate the filter coefficient (alpha) for a first-order IIR low-pass filter
    float dt = 1.0f / sampling_freq;
    float rc = 1.0f / (2 * PI * cutoff_freq);
    float alpha = dt / (rc + dt);


    for (uint32_t i = 0; i < data_size; i++) {
        filtered_data_after_lowpass[i] = alpha * data_buffer[i] + (1.0f - alpha) * filtered_data_after_lowpass[i - 1];
    }

}

void apply_avg_filter(const float *data_buffer, float* filtered_data_after_avg_filter, size_t data_size, uint32_t window_size)
{
    float sum = 0;

    for (uint32_t i = 0; i < data_size; i++) {
        if (i >= window_size) {
            sum -= data_buffer[i - window_size];
        }
        sum += data_buffer[i];
        filtered_data_after_avg_filter[i] = sum / (i < window_size ? i + 1 : window_size);
    }
}

size_t count_peaks(const float *data_buffer, size_t data_size, float threshold)
{
    size_t peak_count = 0;
    
    for (size_t i = 1; i < data_size - 1; i++) {
        bool is_peak = (data_buffer[i] > data_buffer[i - 1]) && (data_buffer[i] > data_buffer[i + 1]);
        if (is_peak && data_buffer[i] > threshold) {
            peak_count++;
        }
    }

    return peak_count;
}

const char* detect_activity(const float *data_buffer, size_t data_size)
{
    const size_t buffer_size = 50;
    const float peak_threshold = 8.0;
    const size_t high_activity_count = 5;

    if (data_size < buffer_size) {
        return "Error: Insufficient data";
    }

    size_t peak_count = count_peaks(data_buffer, buffer_size, peak_threshold);

    return peak_count > high_activity_count ? "High activity" : "Low activity";
}