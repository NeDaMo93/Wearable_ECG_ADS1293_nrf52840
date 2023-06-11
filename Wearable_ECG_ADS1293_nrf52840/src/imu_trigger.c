#include"imu_trigger.h"
#include <stdio.h>

#ifdef CONFIG_LSM6DSO_TRIGGER
static void trigger_handler(const struct device *dev,
			    const struct sensor_trigger *trig)
{
	printf("TRIGGER MODE\n");

	/*
	Hier soll der Thread geweckt werden-
	*/

}

static void test_trigger_mode(const struct device *dev)
{
	struct sensor_trigger trig;

	if (set_sampling_freq(dev) != 0)
		return;

	trig.type = SENSOR_TRIG_DATA_READY;
	trig.chan = SENSOR_CHAN_ACCEL_XYZ;

	if (sensor_trigger_set(dev, &trig, trigger_handler) != 0) {
		printf("Could not set sensor type and channel\n");
		return;
	}
}

#else
static void test_polling_mode(const struct device *dev)
{
	if (set_sampling_freq(dev) != 0) {
		return;
	}

	while (1) {
		printf("POOLING MODE\n");
		//fetch_and_display_IMU_val(dev);
		k_sleep(K_MSEC(1000));
	}
}
#endif
