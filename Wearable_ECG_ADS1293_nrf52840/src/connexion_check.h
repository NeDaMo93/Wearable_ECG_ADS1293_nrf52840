#ifndef CONNEXION_CHECK_H
#define CONNEXION_CHECK_H

#include <zephyr/kernel.h>
#include <zephyr/device.h>
#include <zephyr/bluetooth/conn.h>

void connected(struct bt_conn *conn, uint8_t err);
void disconnected(struct bt_conn *conn, uint8_t reason);

#endif // CONNEXION_CHECK_H
