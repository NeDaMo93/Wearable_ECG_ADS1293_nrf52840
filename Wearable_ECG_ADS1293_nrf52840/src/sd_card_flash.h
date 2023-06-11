#ifndef SD_CARD_FLASH_H
#define SD_CARD_FLASH_H

#include <zephyr/kernel.h>
#include <zephyr/device.h>
#include <zephyr/storage/disk_access.h>
#include <zephyr/logging/log.h>
#include <zephyr/fs/fs.h>
#include <zephyr/kernel.h>
#include <stdio.h>
#include <string.h>
#include <ff.h>

//void flash_to_sd_cardd(struct fs_file_t *file, float value_to_write, uint32_t time);
void flash_ecg_data_to_sd_card(struct fs_file_t *file, uint32_t *buffer);
void flash_imu_data_to_sd_card(struct fs_file_t *file, uint32_t *buffer);
char *generate_file_name(int file_number, const char *sensor_name);

#endif // SD_CARD_FLASH_H