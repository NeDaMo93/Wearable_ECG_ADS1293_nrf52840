#include"lsm6dso.h"
#include <zephyr/drivers/sensor.h>
#include <soc.h>
#include <math.h>
#include <stdio.h>
#include <zephyr/device.h>

//uint32_t body_position;
static float absolute_value(float x, float y, float z){

	float abs;
	abs = sqrt(x*x+y*y+z*z);
	return abs;
}

static inline float out_ev(struct sensor_value *val)
{
	return (val->val1 + (float)val->val2 / 1000000);
}

// MagAccData fetch_and_display(const struct device *dev) {
//     struct sensor_value x, y, z;
//     static int trig_cnt;
//     float mag_acc_vec;
//     int err;
//     trig_cnt++;
//     uint32_t mag_acc_result;

//     /* lsm6dso accel */
//     err = sensor_sample_fetch_chan(dev, SENSOR_CHAN_ACCEL_XYZ);
//     if (err) {
//         printk("sensor_sample_fetch_chan() failed, error: %d\n", err);
//         return (MagAccData) {0, 0};
//     }

//     err = sensor_channel_get(dev, SENSOR_CHAN_ACCEL_X, &x);
//     if (err) {
//         printk("sensor_channel_get() for X failed, error: %d\n", err);
//         return (MagAccData) {0, 0};
//     }

//     err = sensor_channel_get(dev, SENSOR_CHAN_ACCEL_Y, &y);
//     if (err) {
//         printk("sensor_channel_get() for Y failed, error: %d\n", err);
//         return (MagAccData) {0, 0};
//     }

//     err = sensor_channel_get(dev, SENSOR_CHAN_ACCEL_Z, &z);
//     if (err) {
//         printk("sensor_channel_get() for Z failed, error: %d\n", err);
//         return (MagAccData) {0, 0};
//     }
// 	printf("read IMU\n");
// 	/* Determine the axis pointing in the direction of gravitational force */
//     if (x.val1 > 0 && fabs(x.val1) > fabs(y.val1) && fabs(x.val1) > fabs(z.val1)) {
//         printf("Standing Up\n");
//     } else if (x.val1 < 0 && fabs(x.val1) > fabs(y.val1) && fabs(x.val1) > fabs(z.val1)) {
//         printf("Handstand\n");
//     } else if (y.val1 > 0 && fabs(y.val1) > fabs(x.val1) && fabs(y.val1) > fabs(z.val1)) {
//         printf("Lying sideways left side\n");
//     } else if (y.val1 < 0 && fabs(y.val1) > fabs(x.val1) && fabs(y.val1) > fabs(z.val1)) {
//         printf("Lying sideways right side\n");
//     } else if (z.val1 > 0 && fabs(z.val1) > fabs(x.val1) && fabs(z.val1) > fabs(y.val1)) {
//         printf("Lying on Back\n");
//     } else if (z.val1 < 0 && fabs(z.val1) > fabs(x.val1) && fabs(z.val1) > fabs(y.val1)) {
//         printf("Lying on Stomach\n");
//     } else {
//         printf("No significant gravitational force detected.\n");
//     }
//     //calculate the magnitude of the acceleration vector acc_vec
//     mag_acc_vec = absolute_value(out_ev(&x), out_ev(&y), out_ev(&z));
//     mag_acc_result = mag_acc_vec * 1000000;
//     printf("mag_acc_result = %f\n", mag_acc_vec);

//     return (MagAccData) {mag_acc_result, mag_acc_vec};
// }

MagAccData fetch_and_display_IMU_val(const struct device *dev)
{
	struct sensor_value x, y, z;
	static int trig_cnt;
	float mag_acc_vec;
	int err;
	trig_cnt++;
	uint32_t mag_acc_result;
    uint32_t body_position;
    

	/* lsm6dso accel */
	err = sensor_sample_fetch_chan(dev, SENSOR_CHAN_ACCEL_XYZ);
	if (err) {
		printk("sensor_sample_fetch_chan() failed, error: %d\n", err);
		return (MagAccData) {0, 0};
	}

	err = sensor_channel_get(dev, SENSOR_CHAN_ACCEL_X, &x);
	if (err) {
		printk("sensor_channel_get() for X failed, error: %d\n", err);
		return (MagAccData) {0, 0};
	}

	err = sensor_channel_get(dev, SENSOR_CHAN_ACCEL_Y, &y);
	if (err) {
		printk("sensor_channel_get() for Y failed, error: %d\n", err);
		return (MagAccData) {0, 0};
	}

	err = sensor_channel_get(dev, SENSOR_CHAN_ACCEL_Z, &z);
	if (err) {
		printk("sensor_channel_get() for Z failed, error: %d\n", err);
		return (MagAccData) {0, 0};
	}

    /* Determine the axis pointing in the direction of gravitational force */
    /*
    No Signal = 0
    Standing Up = 1
    Handstand = 2
    Lying sideways left side = 3
    Lying sideways right side = 4
    Lying on Back = 5
    Lying on stomach = 6
    */
    if (x.val1 > 0 && fabs(x.val1) > fabs(y.val1) && fabs(x.val1) > fabs(z.val1)) {
        //printf("Standing Up\n");
        body_position = 1;
    } else if (x.val1 < 0 && fabs(x.val1) > fabs(y.val1) && fabs(x.val1) > fabs(z.val1)) {
        //printf("Handstand\n");
        body_position = 2;
    } else if (y.val1 > 0 && fabs(y.val1) > fabs(x.val1) && fabs(y.val1) > fabs(z.val1)) {
        //printf("Lying sideways left side\n");
        body_position = 3;
    } else if (y.val1 < 0 && fabs(y.val1) > fabs(x.val1) && fabs(y.val1) > fabs(z.val1)) {
        //printf("Lying sideways right side\n");
        body_position = 4;
    } else if (z.val1 > 0 && fabs(z.val1) > fabs(x.val1) && fabs(z.val1) > fabs(y.val1)) {
        //printf("Lying on Back\n");
        body_position = 5;
    } else if (z.val1 < 0 && fabs(z.val1) > fabs(x.val1) && fabs(z.val1) > fabs(y.val1)) {
        //printf("Lying on Stomach\n");
        body_position = 6;
    } else {
        //printf("No significant gravitational force detected.\n");
        body_position = 0;
    }

	//calculate the magnitude of the acceleration vector acc_vec
	mag_acc_vec = absolute_value(out_ev(&x), out_ev(&y), out_ev(&z));
	mag_acc_result = mag_acc_vec * 1000000;
    return (MagAccData) {mag_acc_result, body_position};
	//return mag_acc_result;
}