# Wearable_ECG_ADS1293_nrf52840
Software for a Functional Demonstrator of a Medical Wearable for ECG and Activity Monitoring with Wireless Communication. With ADS1293, nrf52840, LSM6DSO. Includes an Android App and Python GUI.
# Summary of the research project
This research project aims to design and develop a functional demonstrator of a portable device for long-term Electrocardiogram (ECG) measurements. The proposed device intends to address the need for continuous and non-invasive monitoring of cardiac activity over extended periods, providing valuable insights into the diagnosis and management of cardiovascular conditions.
In addition to enabling long-term Electrocardiogram (ECG) measurements, the developed wearable device will also incorporate advanced sensors to monitor body position, physical activity, and body temperature continuously. This comprehensive approach aims to provide a holistic assessment of the user's physiological status, allowing for a more thorough evaluation of their overall health. 
The primary objective of this project is to create a working prototype that can serve as a foundation for future advancements in the field. The successful development of this device will facilitate subsequent research projects, fostering innovation and further exploration of long-term ECG monitoring technologies at our institution.
Throughout the research process, various aspects, such as hardware design, signal processing algorithms, power efficiency, and user-friendliness, will be carefully considered and optimized to ensure the device's reliability and practicality. Additionally, emphasis will be placed on achieving a cost-effective solution without compromising the quality of data collected.
Ultimately, the successful completion of this project will lay the groundwork for subsequent investigations, encouraging a continuous cycle of research and development in the domain of portable long-term ECG monitoring. The findings and insights gained from this study will contribute significantly to the improvement of cardiac healthcare and enhance the quality of life for individuals with cardiovascular conditions.
# Hardware Architecture
Based on the requirements, the concept architecture and associated functions could be defined by using specific hardware, which is shown in the following figure. For the demonstrator, Nordic's nRF52840 microcontroller acts as the central element, which is available in the form of a developer board from Adafruit Feather. This board has several integrated functions, including push buttons for triggering external events, LEDs for status indication, and an energy-efficient Bluetooth module. A matching SD card module from Adafruit allows local storage of recorded data. The ADS1293 is used as the analog front end for processing ECG signals. The user's activity level and body position are detected with the help of the ST LSM6DSO IMU. Body temperature is measured by using a PT1000 sensor. The Easypack S rechargeable battery system from Varta supplies the necessary energy. Finally, the collected data is transmitted via Bluetooth to a PC and Android smartphone and displayed.
![image](https://github.com/NeDaMo93/Wearable_ECG_ADS1293_nrf52840/assets/129444601/6c9f3e52-6922-4487-8033-404cfb4e0cb9)
# Software Architecture
The firmware is designed to efficiently handle data acquisition from the ECG and IMU (Inertial Measurement Unit) sensors and transmit the collected data via Bluetooth Low Energy (BLE) while storing it on an SD card. The architecture is implemented using six components, including four separate threads and two interrupt Service Routines (ISRs).
![image](https://github.com/NeDaMo93/Wearable_ECG_ADS1293_nrf52840/assets/129444601/98fc3b22-9218-41b2-bef6-0efa0dbc6aca)
# User Interfaces
## Anrdoid App
The Android application was constructed using the open-source code provided by Nordic for the Android-nRF-Blinky application. 
The Android-nRF-Blinky application, written in Kotlin, serves as an excellent introduction for developers unfamiliar with Bluetooth Low Energy (BLE) and can serve as a template for other projects.
The application uses the Android BLE Library to manage its Bluetooth connections. When the application starts, it scans for nearby BLE devices. Once a device is selected, the application connects to it and discovers its services and characteristics (using the GATT protocol). 
![image](https://github.com/NeDaMo93/Wearable_ECG_ADS1293_nrf52840/assets/129444601/ac5b1322-9ff3-4783-b372-fd197532506b)
## Python GUI
The Python-based graphical user interface (GUI) connects the ECG device via Bluetooth Low Energy (BLE) and visualizes real-time ECG data. It also displays the patient's activity level, body temperature, heart rate and body posture. The code utilizes PyQt5 for GUI development, Matplotlib for plotting, asyncio for asynchronous tasks, Bleak for BLE communication, and biosppy for ECG signal processing.
![image](https://github.com/NeDaMo93/Wearable_ECG_ADS1293_nrf52840/assets/129444601/5a1ee449-d32c-4730-ab91-6154bb457371)

# LBS Files
lbs.h and lbs.c must be replaced in your zephyr directory <br>
**lbs.h:**<br>
..\nrf\include\bluetooth\services<br>
**lbs.c:**<br>
..\nrf\subsys\bluetooth\services<br>
