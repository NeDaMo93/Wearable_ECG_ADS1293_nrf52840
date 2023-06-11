#ifndef BLUETOOTH_CONNECTION_H
#define BLUETOOTH_CONNECTION_H

#include <zephyr/kernel.h>
#include <dk_buttons_and_leds.h>
#include <zephyr/bluetooth/bluetooth.h>
#include <bluetooth/services/lbs.h>

void Bluetooth_init(void);

#endif // BLUETOOTH_CONNECTION_H
