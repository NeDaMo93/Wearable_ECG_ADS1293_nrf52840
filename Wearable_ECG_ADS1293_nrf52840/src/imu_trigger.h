#ifndef IMU_TRIGGER_H
#define IMU_TRIGGER_H


#include <zephyr/device.h>
#include <zephyr/drivers/sensor.h>

#ifdef CONFIG_LSM6DSO_TRIGGER
static void trigger_handler(const struct device *dev,
			    const struct sensor_trigger *trig);

static void test_trigger_mode(const struct device *dev);

#else
static void test_polling_mode(const struct device *dev);

#endif //CONFIG_LSM6DSO_TRIGGER

#endif // imu_trigger_h
