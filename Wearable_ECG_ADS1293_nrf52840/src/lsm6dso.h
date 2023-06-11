#ifndef LSM6DSO_H
#define LSM6DSO_H

#include <zephyr/kernel.h>
#include <zephyr/device.h>

typedef struct {
    uint32_t mag_accelerometer;
    uint32_t body_posture;
} MagAccData;

//MagAccData fetch_and_display(const struct device *dev);
MagAccData fetch_and_display_IMU_val(const struct device *dev);
//uint32_t body_position;

#endif // LSM6DSO_H
