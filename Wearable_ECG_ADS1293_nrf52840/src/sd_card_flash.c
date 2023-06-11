#include "sd_card_flash.h"
#include <zephyr/fs/fs_interface.h>
#include <zephyr/kernel.h>
#include <zephyr/device.h>
#include <zephyr/storage/disk_access.h>
#include <zephyr/logging/log.h>
#include <zephyr/fs/fs.h>
#include <zephyr/kernel.h>
#include <stdio.h>
#include <string.h>
#include <ff.h>
#include <stdio.h>
#include <zephyr/sys/timeutil.h>


LOG_MODULE_REGISTER(flash_to_sd_card);

char *generate_file_name(int file_number, const char *sensor_name)
{
    static char file_name[64];

    snprintf(file_name, sizeof(file_name), "/SD:/%s%d.csv", sensor_name, file_number);

    return file_name;
}
static uint32_t value_index = 1;

void flash_ecg_data_to_sd_card(struct fs_file_t *file, uint32_t *buffer) {

    static bool header_written = false;
    int ret;

    if (!header_written) {
        // Write the header
        const char *header = "Time;ECG_Channel_1;ECG_Channel_2\n";
        ssize_t bytes_written = fs_write(file, header, strlen(header));
        if (bytes_written < 0) {
            printk("Error writing header to file: %zd\n", bytes_written);
            return;
        }
        header_written = true;
    }

    char data_buffer[128];
    ssize_t bytes_written;

    for(int i=0; i<4; i++) {
        // Write values for ECG channel 1 and 2
        snprintf(data_buffer, sizeof(data_buffer), "%u;%u;%u\n", 
            value_index, buffer[i*2], buffer[i*2 + 1]);

        //printk("Successfully wrote Time %u, ECG Channel 1 value %u, ECG Channel 2 value %u to the file\n", 
         //value_index, buffer[i*2], buffer[i*2 + 1]);

        bytes_written = fs_write(file, data_buffer, strlen(data_buffer));
        if (bytes_written < 0) {
            printk("Error writing to file: %zd\n", bytes_written);
            return;
        }

        value_index++;
    }

    ret = fs_sync(file);
    if (ret < 0) {
        printk("Error syncing file: %d\n", ret);
    }
}

static uint32_t imu_value_index = 1;

void flash_imu_data_to_sd_card(struct fs_file_t *file, uint32_t *buffer) {
    static bool header_written = false;
    int ret;

    if (!header_written) {
        // Write the header
        const char *header = "Time;IMU_Data\n";
        ssize_t bytes_written = fs_write(file, header, strlen(header));
        if (bytes_written < 0) {
            printk("Error writing header to file: %zd\n", bytes_written);
            return;
        }
        header_written = true;
    }

    char data_buffer[128];

    for (int i = 0; i < 4; ++i) {
        // Write IMU data value
        snprintf(data_buffer, sizeof(data_buffer), "%u;%.6f\n", 
            imu_value_index, buffer[i]/1000000.0f);

        ssize_t bytes_written = fs_write(file, data_buffer, strlen(data_buffer));
        if (bytes_written < 0) {
            printk("Error writing to file: %zd\n", bytes_written);
            return;
        }
        //printk("Successfully wrote Time %u, IMU Data value %u to the file\n", 
            //imu_value_index, buffer[i]);

        imu_value_index++;
    }

     // Sync after writing
        ret = fs_sync(file);
        if (ret < 0) {
            printk("Error syncing file: %d\n", ret);
    }
}
