/*
 * Research project 2023/2024
 */

/**************************************ZEPHYR LIB INCLUDES***********************************************/
#include <zephyr/types.h>
#include <stddef.h>
#include <string.h>
#include <errno.h>
#include <zephyr/sys/printk.h>
#include <zephyr/sys/byteorder.h>
#include <zephyr/kernel.h>
#include <zephyr/drivers/gpio.h>
#include <soc.h>
#include <zephyr/bluetooth/bluetooth.h>
#include <zephyr/bluetooth/hci.h>
#include <zephyr/bluetooth/conn.h>
#include <zephyr/bluetooth/uuid.h>
#include <zephyr/bluetooth/gatt.h>
#include <zephyr/drivers/sensor.h>
#include <math.h>
#include <stdio.h>
#include <stdbool.h>
#include <time.h>
#include <zephyr/kernel.h>
#include <zephyr/device.h>
#include <bluetooth/services/lbs.h>
#include <zephyr/settings/settings.h>
#include <dk_buttons_and_leds.h>
#include <zephyr/logging/log.h>
#include <zephyr/drivers/spi.h>  // provides access to SPI driver features
#include <zephyr/drivers/gpio.h>  // provides access to GPIO driver features
#include <hal/nrf_gpio.h>

/************************************** HEADER FILES INCLUDES ***********************************************/
#include "bluetooth_connection.h"
#include"lsm6dso.h"
#include"connexion_check.h"
#include "sd_card_flash.h"
#include"detect_activity.h"
#include"ADS1293.h"
#include"imu_trigger.h"
#include"Queue.h"
#include"temperatureSens.h"
#include "batteryManagement.h"

/************************************** THREADS SEMAPHORES ***********************************/
//Define sd-card semaphore
K_SEM_DEFINE(sd_card_sem, 1, 1); 
//Define semaphore for imu thread
K_SEM_DEFINE(imu_sem, 0, 1); 
//Define semaphore for ecg thread
K_SEM_DEFINE(ecg_sem, 0, 1); 
//Define semaphore for temperature thread
K_SEM_DEFINE(temp_sem, 0, 1); 

/************************************** DEFINE THREADS ****************************************/
K_THREAD_STACK_DEFINE(ecg_thread_stack_area, 2048);
K_THREAD_STACK_DEFINE(imu_thread_stack_area, 2048);
K_THREAD_STACK_DEFINE(temp_thread_stack_area, 2048);
struct k_thread ecg_thread_data;
struct k_thread imu_thread_data;
struct k_thread temp_thread_data;
K_MUTEX_DEFINE(sensor_data_mutex);
K_MUTEX_DEFINE(queue_data_mutex);

/************************************** GLOBALE VARIABLE  *************************************/
typedef struct {
    uint16_t channel_1_ecgVal;
    uint16_t channel_2_ecgVal;
} Ecg_values_norm;
//4232264
uint32_t offset_ecg = 4238892;
uint32_t last_interrupt_time = 0;
uint64_t last_imu_read_time = 0;
uint32_t imu_activity_buffer [4];
uint16_t temp_posture_buffer [2];
uint32_t raw_ecg_buffer[8];
uint16_t ecg_buffer[8];
Queue* imu_activity_queue = NULL; 
uint32_t body_posture;
float body_temperature;
uint32_t bodyTemp_toBe_send;
bool temperature_flag = false;
volatile bool ecg_data_sent = false;
K_MUTEX_DEFINE(ble_data_mutex);
LOG_MODULE_REGISTER(main);

// power saving for battery temp
#define POWER NRF_GPIO_PIN_MAP(0,28)
/************************************** STATIC VARIABLES **************************************/
static int lsdir(const char *path);
static FATFS fat_fs;
static int file_count = 0;
struct fs_file_t ecg_file;
struct fs_file_t imu_file;

/************************************** SD CARD MOUNTING ***************************************/
/* mounting info */
static struct fs_mount_t mp = {
	.type = FS_FATFS,
	.fs_data = &fat_fs,
};
static const char *disk_mount_pt = "/SD:";

/************************************** IMU SAMPLING FREQUENCY *********************************/
static int set_sampling_freq(const struct device *dev)
{
	int ret_gpio = 0;
	struct sensor_value odr_attr;

	/* set accel sampling frequency to 208 Hz */
	odr_attr.val1 = 208;
	odr_attr.val2 = 0;

	ret_gpio = sensor_attr_set(dev, SENSOR_CHAN_ACCEL_XYZ,
			SENSOR_ATTR_SAMPLING_FREQUENCY, &odr_attr);
	if (ret_gpio != 0) {
		printf("Cannot set sampling frequency for accelerometer.\n");
		return ret_gpio;
	}

	ret_gpio = sensor_attr_set(dev, SENSOR_CHAN_GYRO_XYZ,
			SENSOR_ATTR_SAMPLING_FREQUENCY, &odr_attr);
	if (ret_gpio != 0) {
		printf("Cannot set sampling frequency for gyro.\n");
		return ret_gpio;
	}

	return 0;
}

/************************************** SD CARD LISTDIR ************************************************/
// SD
static int lsdir(const char *path)
{
    int res;
    struct fs_dir_t dirp;
    static struct fs_dirent entry;
    int count = 0;

    fs_dir_t_init(&dirp);

    res = fs_opendir(&dirp, path);
    if (res) {
        printk("Error opening dir %s [%d]\n", path, res);
        return res;
    }

    printk("\nListing dir %s ...\n", path);
    while (1) {
        res = fs_readdir(&dirp, &entry);
        if (res || entry.name[0] == 0) {
            break;
        }

        if (entry.type != FS_DIR_ENTRY_DIR) {
            count++;
            if (count % 2 == 0) {
                file_count++;
            }
        }

        if (entry.type == FS_DIR_ENTRY_DIR) {
            printk("[DIR ] %s\n", entry.name);
        } else {
            printk("[FILE] %s (size = %zu)\n",
                entry.name, entry.size);
        }
    }

    fs_closedir(&dirp);

    return res;
}

/************************************** CALCULATE FREQUENCY  ************************************************/
double calculate_freq(uint32_t current_time)
{
	uint32_t interrupt_period;
	double frequency_hz;

	if (last_interrupt_time != 0) {
		if (current_time >= last_interrupt_time) {
			interrupt_period = current_time - last_interrupt_time;
		} else {
			// handle overflow
			interrupt_period = UINT32_MAX - last_interrupt_time + current_time;
		}

		// Convert to nanoseconds
		uint64_t interrupt_period_ns = k_cyc_to_ns_ceil64(interrupt_period);

		// Calculate frequency (Hz) = 1 / period (s)
		// The period is in nanoseconds, so we need to divide by 1e9 to convert to seconds
		double period_s = (double)interrupt_period_ns / 1e9;

		// Calculate frequency
		frequency_hz = 1.0 / period_s;

		//printf("Interrupt frequency: %.2f Hz\n", frequency_hz);
	}

	last_interrupt_time = current_time;
	return frequency_hz;

}

/************************************** ECG THREAD ************************************************/
void ecg_data_acquisition_thread(void *unused1, void *unused2, void *unused3) {
	int ecg_buffer_ind = 0;
	
    while (1) {
		
		Ecg_values_norm ecg_values_norm;
		// wait for semaphore
		k_sem_take(&ecg_sem, K_FOREVER); // the semaphore count goes down to 0

		ECG_Values ecg_values = read_ecg_values();

		ecg_values_norm.channel_1_ecgVal = ecg_values.channel_1_ecgVal - offset_ecg;
		ecg_values_norm.channel_2_ecgVal = ecg_values.channel_2_ecgVal - offset_ecg;
		///printf(", Values norm: %u, %u\n", ecg_values_norm.channel_1_ecgVal , ecg_values_norm.channel_2_ecgVal);

		//calculate frequency
		uint32_t current_time = k_cycle_get_32();
		double frequency_hz = calculate_freq(current_time);

		//fill buffer with norm. values and raw values
		ecg_buffer[ecg_buffer_ind] = ecg_values_norm.channel_1_ecgVal;
		raw_ecg_buffer[ecg_buffer_ind] = ecg_values.channel_1_ecgVal;
		ecg_buffer_ind++;
		ecg_buffer[ecg_buffer_ind] = ecg_values_norm.channel_2_ecgVal;
		raw_ecg_buffer[ecg_buffer_ind] = ecg_values.channel_2_ecgVal;
		ecg_buffer_ind = (ecg_buffer_ind+1)%8;

		// wake up IMU thread to synchronize READ DATA 
		k_sem_give(&imu_sem);

		if ( ecg_buffer_ind == 0){
			
			//BLE send ECG packet, is blocked to avoid BLE conflicts, could also work with threads flags
			k_mutex_lock(&ble_data_mutex, K_FOREVER);
		    send_ECG_Packet_Data(ecg_buffer, sizeof(ecg_buffer));
			k_mutex_unlock(&ble_data_mutex);

			//flash data to sd card
			k_sem_take(&sd_card_sem, K_FOREVER); // Take the semaphore

			flash_ecg_data_to_sd_card(&ecg_file, raw_ecg_buffer);
			k_sem_give(&sd_card_sem); // Give the semaphore
			
			// Clear the buffer
            for (int i = 0; i < 8; i++) {
                ecg_buffer[i] = 0;
            }

		}
    }
}

/************************************** IMU THREAD ************************************************/

void imu_data_acquisition_thread(void *unused1, void *unused2, void *unused3) {

	int imu_activity_buffer_ind = 0;
	uint32_t last_fetch_time = 0;
	uint32_t current_t;
    while (1) {
		
		/* Wait until semaphore is called */
        k_sem_take(&imu_sem, K_FOREVER); // the semaphore count goes down to 0
		
		//calculate the mean value of the values saved on the queue, and then dequeue the queue to liberate space

		uint32_t mean_imu_val = calculateMean(imu_activity_queue);

		imu_activity_buffer[imu_activity_buffer_ind] = mean_imu_val;
		imu_activity_buffer_ind = (imu_activity_buffer_ind+1)%4;
		
		//dequeue the queue
		k_mutex_lock(&queue_data_mutex, K_FOREVER);
		emptyQueue(imu_activity_queue);
		k_mutex_unlock(&queue_data_mutex);

		if (imu_activity_buffer_ind == 0){
		current_t= k_uptime_get();
		//read temperature value
		bodyTemp_toBe_send = body_temperature *(100); //covert float to uint32_t
			
		//fill temp_posture_buffer
		temp_posture_buffer[0] = bodyTemp_toBe_send;
		temp_posture_buffer[1] = body_posture;

		printf("Temperature = %u \n",temp_posture_buffer[0] );
		
		k_mutex_lock(&ble_data_mutex, K_FOREVER);
		send_temp_IMU_Packet_Data(imu_activity_buffer, sizeof(imu_activity_buffer));
		k_mutex_unlock(&ble_data_mutex);

		//BLE send Temperature and body-posture packet
		
		if (current_t - last_fetch_time >= 3500) //3500 is adjustable, to avoid sending 3 packet simultaneously 
		{
		k_sem_give(&temp_sem); //give semaphore to read temperature values
		send_temperatur_body_posture_Packet(temp_posture_buffer, sizeof(temp_posture_buffer));
		last_fetch_time = current_t;
     	}
	
		//write to sd card

		k_sem_take(&sd_card_sem, K_FOREVER); // Take the semaphore to write to sd card
		k_mutex_lock(&sensor_data_mutex, K_FOREVER);
		flash_imu_data_to_sd_card(&imu_file, imu_activity_buffer);
		k_mutex_unlock(&sensor_data_mutex);
		k_sem_give(&sd_card_sem); 

		// Clear the buffer
		for (int i = 0; i < 4; i++) { 
			imu_activity_buffer[i] = 0;
		}
		}
    }
}

/************************************** TEMP THREAD ************************************************/
void temp_data_acquisition_thread()
{
	while(1)
	{
		k_sem_take(&temp_sem, K_FOREVER); // the semaphore count goes down to 0
		body_temperature = get_bodyTemperature();
		nrf_gpio_pin_set(POWER);
		get_battery_info();
		nrf_gpio_pin_clear(POWER);
	}

}

/************************************** DETECT SD CARD ************************************************/
void mount_sd_card()
{
	/* sd card detected? */
	do {
		static const char *disk_pdrv = "SD";
		uint64_t memory_size_mb;
		uint32_t block_count;
		uint32_t block_size;

		if (disk_access_init(disk_pdrv) != 0) {
			LOG_ERR("Storage init ERROR!");
			break;
		}

		if (disk_access_ioctl(disk_pdrv,
				DISK_IOCTL_GET_SECTOR_COUNT, &block_count)) {
			LOG_ERR("Unable to get sector count");
			break;
		}
		LOG_INF("Block count %u", block_count);

		if (disk_access_ioctl(disk_pdrv,
				DISK_IOCTL_GET_SECTOR_SIZE, &block_size)) {
			LOG_ERR("Unable to get sector size");
			break;
		}
		printk("Sector size %u\n", block_size);

		memory_size_mb = (uint64_t)block_count * block_size;
		printk("Memory Size(MB) %u\n", (uint32_t)(memory_size_mb >> 20));
	} while (0);

	mp.mnt_point = disk_mount_pt;
	int res = fs_mount(&mp);

	if (res == FR_OK) {
		printk("Disk mounted.\n");
		lsdir(disk_mount_pt);
	} else {
		printk("Error mounting disk.\n");
	}
}

/************************************** CREAT IMU/ECG/Temp THREADS ************************************************/
void start_ecg_data_acquisition_thread(void) {
    k_thread_create(&ecg_thread_data, ecg_thread_stack_area, K_THREAD_STACK_SIZEOF(ecg_thread_stack_area), 
                    ecg_data_acquisition_thread, NULL, NULL, NULL, 1, 0, K_NO_WAIT);
}

void start_imu_data_acquisition_thread(void) {
    k_thread_create(&imu_thread_data, imu_thread_stack_area, K_THREAD_STACK_SIZEOF(imu_thread_stack_area), 
                    imu_data_acquisition_thread, NULL, NULL, NULL, 2, 0, K_NO_WAIT);
}

void start_temperature_data_acquisition_thread(void) {
    k_thread_create(&temp_thread_data, temp_thread_stack_area, K_THREAD_STACK_SIZEOF(temp_thread_stack_area), 
                    temp_data_acquisition_thread, NULL, NULL, NULL, 3, 0, K_NO_WAIT);
}

/************************************** IMU TRIGGER/POOLING MODE  ************************************************/

#ifdef CONFIG_LSM6DSO_TRIGGER
static void trigger_handler(const struct device *dev,
			    const struct sensor_trigger *trig)
{
	uint32_t mag_acc_val;
	MagAccData output_imu_sensor = fetch_and_display_IMU_val(dev);
	mag_acc_val = output_imu_sensor.mag_accelerometer;
	body_posture = output_imu_sensor.body_posture;
	k_mutex_lock(&queue_data_mutex, K_FOREVER);
    enQueue(imu_activity_queue, mag_acc_val);
	k_mutex_unlock(&queue_data_mutex);
	
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
		fetch_and_display_IMU_val(dev);
		k_sleep(K_MSEC(1000));
	}
}
#endif

/************************************** IMU POOLING MODE  ************************************************/
//POOLING MODE
static void test_polling_mode(const struct device *dev)
{

	if (!device_is_ready(dev)) {
		printf("%s: device not ready.\n", dev->name);
		return;
	}
	printf("Testing LSM6DSO sensor in polling mode.\n\n");

	if (set_sampling_freq(dev) != 0) {
		return;
	}

	while (1) {
	
	uint32_t mag_acc_val;
	MagAccData output_imu_sensor = fetch_and_display_IMU_val(dev);
	mag_acc_val = output_imu_sensor.mag_accelerometer;
	k_mutex_lock(&queue_data_mutex, K_FOREVER);
    enQueue(imu_activity_queue, mag_acc_val);
	k_mutex_unlock(&queue_data_mutex);

	}
}

/************************************** ADS1293 INTERRUPT CALLBACK **********************************/
void ecg_data_callback(const struct device *dev, struct gpio_callback *cb, uint32_t pins)
{
    ads1293_interrupt = true;
	
	k_sem_give(&ecg_sem); 
}

void main(void)

{	/************************************** LOCAL VARIABLES ******************************************/
	int count_temp_sens = 0;
	/************************************** INITIALIZE IMU SENSOR  **********************************/
	//Initialize IMU sensor
	const struct device *const dev = DEVICE_DT_GET_ONE(st_lsm6dso);
	if (!device_is_ready(dev)) {
		printf("%s: device not ready.\n", dev->name);
		return;
	}
	printf("Testing LSM6DSO sensor in polling mode.\n\n");

	if (set_sampling_freq(dev) != 0) {
		return;
	}
	test_trigger_mode(dev);

	/************************************** INITIALIZE Temperatur SENSOR  ****************************/
	temp_sens_initialization();
	/************************************** SPI INIT ************************************************/
	//check SPI connexion
	ads1293_SPI_init();

	/************************************** GPIO INIT ************************************************/
	//Initialize GPIO Interrupt pin for ecg data
	GPIOinterrupt_init();

	/************************************** Battery Management Setup ******************************************/
	// setup battery management
	battery_management_initialization();
	nrf_gpio_cfg_output(POWER);

	/************************************** SD CARD MOUNTING ******************************************/
	//SD Card mounting
	mount_sd_card();

	/************************************** OPENNING IMU/ECG.CSV FILES **********************************/
	//Opening ECG file
	char *ecg_file_name = generate_file_name(file_count, "ECG");
	int ret_file_ecg;
	ret_file_ecg = fs_open(&ecg_file, ecg_file_name, FS_O_WRITE | FS_O_CREATE | FS_O_APPEND);
	if (ret_file_ecg < 0) {
		printk("Error opening ecg_file: %d\n", ret_file_ecg);
		return;
	}
	printk("succeess to open ecg_file %s\n",ecg_file_name );

	//Opening IMU file
	char *imu_file_name = generate_file_name(file_count, "IMU");
	int ret_file;
	ret_file = fs_open(&imu_file, imu_file_name, FS_O_WRITE | FS_O_CREATE | FS_O_APPEND);
	if (ret_file < 0) {
		printk("Error opening imu_file: %d\n", ret_file);
		return;
	}
	printk("succeess to open imu_file %s\n",imu_file_name );

	/************************************** CREATE IMU QUEUE ************************************************/
	imu_activity_queue = createQueue();

	/************************************** START IMU THREAD************************************************/
	//start imu thread
	start_imu_data_acquisition_thread();

	/************************************** START ECG THREAD ************************************************/
	//Start ecg thread
	start_ecg_data_acquisition_thread();

	/************************************** START TEMPERATURE THREAD ************************************************/
	//Start tempearture thread
	start_temperature_data_acquisition_thread();

	/************************************** BLUETOOTH INIT ************************************************/
	//Bluetooth connection
	Bluetooth_init();

}
