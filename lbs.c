/*
 * Copyright (c) 2018 Nordic Semiconductor ASA
 *
 * SPDX-License-Identifier: LicenseRef-Nordic-5-Clause
 */

/** @file
 *  @brief LED Button Service (LBS) sample
 */

#include <zephyr/types.h>
#include <stddef.h>
#include <string.h>
#include <errno.h>
#include <zephyr/sys/printk.h>
#include <zephyr/sys/byteorder.h>
#include <zephyr/kernel.h>

#include <zephyr/bluetooth/bluetooth.h>
#include <zephyr/bluetooth/hci.h>
#include <zephyr/bluetooth/conn.h>
#include <zephyr/bluetooth/uuid.h>
#include <zephyr/bluetooth/gatt.h>

#include <bluetooth/services/lbs.h>

#include <zephyr/logging/log.h>
#include <math.h> 
#include <stdio.h>


LOG_MODULE_REGISTER(bt_lbs, CONFIG_BT_LBS_LOG_LEVEL);

static bool                   notify_enabled;
static bool                   notify_IMU_Temp_enabled;
static bool                   notify_temp_body_position_enabled;
static bool                   button_state;
static bool 				  temp_imu_data_state;
static bool 				  temp_position_data_state;
static struct bt_lbs_cb       lbs_cb;
static struct bt_lbs_cb       tem_imu_cb;
static struct bt_lbs_cb       tem_position_cb;



static void lbslc_ccc_cfg_changed(const struct bt_gatt_attr *attr,
				  uint16_t value)
{
	notify_enabled = (value == BT_GATT_CCC_NOTIFY);
}

static void temp_imu_ccc_cfg_changed(const struct bt_gatt_attr *attr,
				  uint16_t value)
{
	notify_IMU_Temp_enabled = (value == BT_GATT_CCC_NOTIFY);
}

static void temp_body_position_ccc_cfg_changed(const struct bt_gatt_attr *attr,
				  uint16_t value)
{
	notify_temp_body_position_enabled = (value == BT_GATT_CCC_NOTIFY);
}

static ssize_t write_led(struct bt_conn *conn,
			 const struct bt_gatt_attr *attr,
			 const void *buf,
			 uint16_t len, uint16_t offset, uint8_t flags)
{
	LOG_DBG("Attribute write, handle: %u, conn: %p", attr->handle,
		(void *)conn);

	if (len != 1U) {
		LOG_DBG("Write led: Incorrect data length");
		return BT_GATT_ERR(BT_ATT_ERR_INVALID_ATTRIBUTE_LEN);
	}

	if (offset != 0) {
		LOG_DBG("Write led: Incorrect data offset");
		return BT_GATT_ERR(BT_ATT_ERR_INVALID_OFFSET);
	}

	if (lbs_cb.led_cb) {
		uint8_t val = *((uint8_t *)buf);

		if (val == 0x00 || val == 0x01) {
			lbs_cb.led_cb(val ? true : false);
		} else {
			LOG_DBG("Write led: Incorrect value");
			return BT_GATT_ERR(BT_ATT_ERR_VALUE_NOT_ALLOWED);
		}
	}

	return len;
}

#ifdef CONFIG_BT_LBS_POLL_BUTTON
static ssize_t read_button(struct bt_conn *conn,
			  const struct bt_gatt_attr *attr,
			  void *buf,
			  uint16_t len,
			  uint16_t offset)
{
	const char *value = attr->user_data;

	LOG_DBG("Attribute read, handle: %u, conn: %p", attr->handle,
		(void *)conn);

	if (lbs_cb.button_cb) {
		button_state = lbs_cb.button_cb();
		return bt_gatt_attr_read(conn, attr, buf, len, offset, value,
					 sizeof(*value));
	}

	return 0;
}
#endif 

#ifdef CONFIG_BT_LBS_POLL_BUTTON
static ssize_t read_IMU_Temp(struct bt_conn *conn,
			  const struct bt_gatt_attr *attr,
			  void *buf,
			  uint16_t len,
			  uint16_t offset)
{
	const char *value = attr->user_data;

	LOG_DBG("Attribute read, handle: %u, conn: %p", attr->handle,
		(void *)conn);

	if (tem_imu_cb.button_cb) {
		temp_imu_data_state = tem_imu_cb.button_cb();
		return bt_gatt_attr_read(conn, attr, buf, len, offset, value,
					 sizeof(*value));
	}

	return 0;
}
#endif 

#ifdef CONFIG_BT_LBS_POLL_BUTTON
static ssize_t read_Temp_body_position(struct bt_conn *conn,
			  const struct bt_gatt_attr *attr,
			  void *buf,
			  uint16_t len,
			  uint16_t offset)
{
	const char *value = attr->user_data;

	LOG_DBG("Attribute read, handle: %u, conn: %p", attr->handle,
		(void *)conn);

	if (tem_position_cb.button_cb) {
		temp_position_data_state = tem_position_cb.button_cb();
		return bt_gatt_attr_read(conn, attr, buf, len, offset, value,
					 sizeof(*value));
	}

	return 0;
}
#endif 

/* Service declaration*/
BT_GATT_SERVICE_DEFINE(lbs_svc,
BT_GATT_PRIMARY_SERVICE(BT_UUID_LBS),
#ifdef CONFIG_BT_LBS_POLL_BUTTON
    BT_GATT_CHARACTERISTIC(BT_UUID_ECG_DATA,
                           BT_GATT_CHRC_READ | BT_GATT_CHRC_NOTIFY,
                           BT_GATT_PERM_READ, read_button, NULL,
                           &button_state),
#else
    BT_GATT_CHARACTERISTIC(BT_UUID_ECG_DATA,
                           BT_GATT_CHRC_READ | BT_GATT_CHRC_NOTIFY,
                           BT_GATT_PERM_READ, NULL, NULL, NULL),
#endif
    BT_GATT_CCC(lbslc_ccc_cfg_changed,
                BT_GATT_PERM_READ | BT_GATT_PERM_WRITE),

#ifdef CONFIG_BT_LBS_POLL_BUTTON
    BT_GATT_CHARACTERISTIC(Temp_IMU_UUID,
                           BT_GATT_CHRC_READ | BT_GATT_CHRC_NOTIFY,
                           BT_GATT_PERM_READ, read_IMU_Temp, NULL,
                           &temp_imu_data_state),
#else
    BT_GATT_CHARACTERISTIC(Temp_IMU_UUID,
                           BT_GATT_CHRC_READ | BT_GATT_CHRC_NOTIFY,
                           BT_GATT_PERM_READ, NULL, NULL, NULL),
#endif
    BT_GATT_CCC(temp_imu_ccc_cfg_changed,
                BT_GATT_PERM_READ | BT_GATT_PERM_WRITE),

#ifdef CONFIG_BT_LBS_POLL_BUTTON
    BT_GATT_CHARACTERISTIC(Temp_BODY_POSITION_UUID,
                           BT_GATT_CHRC_READ | BT_GATT_CHRC_NOTIFY,
                           BT_GATT_PERM_READ, read_IMU_Temp, NULL,
                           &temp_position_data_state),
#else
    BT_GATT_CHARACTERISTIC(Temp_BODY_POSITION_UUID,
                           BT_GATT_CHRC_READ | BT_GATT_CHRC_NOTIFY,
                           BT_GATT_PERM_READ, NULL, NULL, NULL),
#endif
    BT_GATT_CCC(temp_body_position_ccc_cfg_changed,
                BT_GATT_PERM_READ | BT_GATT_PERM_WRITE),
);



int bt_lbs_init(struct bt_lbs_cb *callbacks)
{
	if (callbacks) {
		lbs_cb.led_cb    = callbacks->led_cb;
		lbs_cb.button_cb = callbacks->button_cb;
	}

	return 0;
}

int send_ECG_Packet_Data(uint32_t *ecg_data_val, size_t buf_size)
{
    if (!notify_enabled) {
        return -EACCES;
    }
    // Send the byte array over Bluetooth using bt_gatt_notify function
    return bt_gatt_notify(NULL, &lbs_svc.attrs[2],
                          ecg_data_val,
                          buf_size);
}

int send_temp_IMU_Packet_Data(uint32_t *mag_acc_vec, size_t buf_size)
{
    if (!notify_IMU_Temp_enabled) {
        return -EACCES;
    }
    // Send the byte array over Bluetooth using bt_gatt_notify function
    return bt_gatt_notify(NULL, &lbs_svc.attrs[5],
                          mag_acc_vec,
                          buf_size);
}

int send_temperatur_body_posture_Packet(uint16_t *mag_acc_vec, size_t buf_size)
{
    if (!notify_temp_body_position_enabled) {
        return -EACCES;
    }
    // Send the byte array over Bluetooth using bt_gatt_notify function
    return bt_gatt_notify(NULL, &lbs_svc.attrs[8],
                          mag_acc_vec,
                          buf_size);
}
struct bt_gatt_service *get_lbs_service(void)
{
    return &lbs_svc;
}
bool get_notify_enabled(void)
{
	return notify_enabled;
}
bool get_notify_IMU_Temp(void)
{
	return notify_IMU_Temp_enabled;
}