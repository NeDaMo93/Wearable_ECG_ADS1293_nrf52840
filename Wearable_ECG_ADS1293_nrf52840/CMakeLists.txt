#
# Copyright (c) 2018 Nordic Semiconductor
#
# SPDX-License-Identifier: LicenseRef-Nordic-5-Clause
#
cmake_minimum_required(VERSION 3.20.0)


find_package(Zephyr REQUIRED HINTS $ENV{ZEPHYR_BASE})
project(NONE)

target_link_libraries(app PUBLIC libm.a)



# Add your source files
target_sources(app PRIVATE src/main.c src/bluetooth_connection.c src/lsm6dso.c src/connexion_check.c src/sd_card_flash.c src/detect_activity.c src/ADS1293.c src/imu_trigger.c src/Queue.c src/temperatureSens.c)

# NORDIC SDK APP END
zephyr_library_include_directories(.)

include_directories(${ZEPHYR_BASE}/include/logging)

