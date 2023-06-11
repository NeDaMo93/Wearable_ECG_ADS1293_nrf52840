#ifndef DETECT_ACTIVITY_H
#define DETECT_ACTIVITY_H
#include<stdio.h>
#include<string.h>

void apply_avg_filter(const float *data_buffer, float* filtered_data_after_avg_filter, size_t data_size, uint32_t window_size);
void apply_low_pass_filter(const float *data_buffer, float* filtered_data_after_lowpass, size_t data_size, float cutoff_freq, float sampling_freq);
size_t count_peaks(const float *data_buffer, size_t data_size, float threshold);
const char* detect_activity(const float *data_buffer, size_t data_size);

#endif