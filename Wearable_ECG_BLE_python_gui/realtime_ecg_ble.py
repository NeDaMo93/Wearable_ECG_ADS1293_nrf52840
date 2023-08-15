# Janniks implementierung von Pulse

from PyQt5 import QtCore, QtWidgets, QtGui
from matplotlib.backends.backend_qt5agg import FigureCanvasQTAgg as FigureCanvas
from matplotlib.figure import Figure
import asyncio
from bleak import BleakScanner, BleakClient
import numpy as np
from qasync import QEventLoop, asyncSlot
from scipy.signal import find_peaks
import time
import biosppy.signals.ecg as ecg
import random



heart_rate_characteristic = '00001524-1212-efde-1523-785feabcd123'
IMU_Temp_level_characteristic = '00001525-1212-efde-1523-785feabcd123'
TEMP_BodyPosition_characteristic = '00001526-1212-efde-1523-785feabcd123'


app = QtWidgets.QApplication([])
loop = QEventLoop(app)
asyncio.set_event_loop(loop)

activityLevel_threshold = 320


class MainWindow(QtWidgets.QMainWindow):
    def __init__(self):
        super().__init__()

        self.sample_rate_counter = 0

        self.plot_window = 400
        self.ecg_buffer_ch1 = np.zeros(self.plot_window)
        self.ecg_buffer_ch2 = np.zeros(self.plot_window)
        self.ecg_buffer2_ch1 = np.zeros(self.plot_window*2)
        self.ecg_buffer2_ch2 = np.zeros(self.plot_window*2)
        self.imu_buffer = np.zeros(self.plot_window)
        self.sample_index = 0

        self.setWindowTitle("Real-time Plot")
        self.centralWidget = QtWidgets.QWidget(self)
        self.setCentralWidget(self.centralWidget)
        
        # Set the background color of the central widget to dark grey
        self.centralWidget.setStyleSheet("background-color: white;")

        self.layout = QtWidgets.QVBoxLayout(self.centralWidget)

        self.figure1 = Figure()
        self.canvas1 = FigureCanvas(self.figure1)
        self.figure1.set_facecolor('xkcd:white')
        self.layout.addWidget(self.canvas1)
        self.axes1 = self.figure1.add_subplot(111)
        self.axes1.set_ylabel("ECG-Channel 1")
        self.axes1.set_facecolor('xkcd:white')
        self.plot_line1, = self.axes1.plot([], [], 'b-')

        # # Add main grid to the subplot
        # self.axes1.grid(True, linestyle='-', linewidth=1, alpha=1.0)
        # self.axes1.set_axisbelow(True)
        # self.axes1.grid(color='gray', linestyle='-', linewidth=0.5)
        # self.axes1.set_xticks(range(0, self.plot_window+1, 100))
        # self.axes1.set_yticks(range(0, self.plot_window+1, 100))
        # self.axes1.set_xlim(0, self.plot_window)
        # self.axes1.set_ylim(0, self.plot_window)

        # # Add subgrid to the subplot
        # self.axes1.grid(True, which='minor', linestyle=':', linewidth=0.5)
        # self.axes1.set_xticks(range(0, self.plot_window+1, 10), minor=True)
        # self.axes1.set_yticks(range(0, self.plot_window+1, 10), minor=True)
        # self.axes1.set_xlim(0, self.plot_window)
        # self.axes1.set_ylim(0, self.plot_window)

        #add space between plots
        self.figure1.subplots_adjust(hspace=0.5)

        self.figure2 = Figure()
        self.canvas2 = FigureCanvas(self.figure2)
        self.figure2.set_facecolor('xkcd:white')
        self.layout.addWidget(self.canvas2)
        self.axes2 = self.figure2.add_subplot(111)
        self.axes2.set_ylabel("ECG-Channel 2")
        self.axes2.set_facecolor('xkcd:white')
        self.plot_line2, = self.axes2.plot([], [], 'r-')
        # # Add main grid to the subplot
        # self.axes2.grid(True, linestyle='-', linewidth=1, alpha=1.0)
        # self.axes2.set_axisbelow(True)
        # self.axes2.grid(color='gray', linestyle='-', linewidth=0.5)
        # self.axes2.set_xticks(range(0, self.plot_window+1, 100))
        # self.axes2.set_yticks(range(0, self.plot_window+1, 100))
        # self.axes2.set_xlim(0, self.plot_window)
        # self.axes2.set_ylim(0, self.plot_window)

        # # Add subgrid to the subplot
        # self.axes2.grid(True, which='minor', linestyle=':', linewidth=0.5)
        # self.axes2.set_xticks(range(0, self.plot_window+1, 10), minor=True)
        # self.axes2.set_yticks(range(0, self.plot_window+1, 10), minor=True)
        # self.axes2.set_xlim(0, self.plot_window)
        # self.axes2.set_ylim(0, self.plot_window)

        #add space between plots
        self.figure1.subplots_adjust(hspace=0.5)

        # Create a label for displaying pulse of chanel 1
        self.pulse_ch1 = QtWidgets.QLabel()
        self.layout.addWidget(self.pulse_ch1, alignment=QtCore.Qt.AlignLeft)  # Set alignment to center
        self.pulse_ch1.setStyleSheet("background-color: white; color: black;")
        self.pulse_ch1.setText(f"Pulse chanel 1: ")
        self.pulse_ch1.setFont(QtGui.QFont("Arial", 12))  # Set font and size

        # Create a label for displaying pulse of chanel 2
        self.pulse_ch2 = QtWidgets.QLabel()
        self.layout.addWidget(self.pulse_ch2, alignment=QtCore.Qt.AlignLeft)  # Set alignment to center
        self.pulse_ch2.setStyleSheet("background-color: white; color: black;")
        self.pulse_ch2.setText(f"Pulse chanel 2: ")
        self.pulse_ch2.setFont(QtGui.QFont("Arial", 12))  # Set font and size

        # Create a label for displaying activity type
        self.activityLabel = QtWidgets.QLabel()
        self.layout.addWidget(self.activityLabel, alignment=QtCore.Qt.AlignLeft)  # Set alignment to center
        self.activityLabel.setStyleSheet("background-color: white; color: black;")
        self.activityLabel.setText(f"Activity-Type: ")
        self.activityLabel.setFont(QtGui.QFont("Arial", 12))  # Set font and size

        # Create labels for displaying IMU values
        self.tempLabel = QtWidgets.QLabel()
        self.layout.addWidget(self.tempLabel, alignment=QtCore.Qt.AlignLeft)  # Align label text to center
        self.tempLabel.setStyleSheet("background-color: white; color: black;")
        self.tempLabel.setFont(QtGui.QFont("Arial", 12))  # Set font and size
        self.tempLabel.setText(f"Body-Temperature: ")
        self.layout.addWidget(self.tempLabel)  

        self.posLabel = QtWidgets.QLabel()
        self.layout.addWidget(self.posLabel, alignment=QtCore.Qt.AlignLeft)  # Align label text to right
        self.posLabel.setStyleSheet("background-color: white; color: black;")
        self.posLabel.setFont(QtGui.QFont("Arial", 12))  # Set font and size
        self.posLabel.setText(f"Body-Posture: ")
        self.layout.addWidget(self.posLabel)  

        #add space between plots
        self.figure1.subplots_adjust(hspace=0.5)

        self.figure3 = Figure()
        self.figure3.set_facecolor('xkcd:white')
        self.canvas3 = FigureCanvas(self.figure3)
        self.layout.addWidget(self.canvas3)
        self.axes3 = self.figure3.add_subplot(111)
        self.axes3.set_xlabel("Sample")
        self.axes3.set_ylabel("Acceleration")
        self.axes3.set_facecolor('xkcd:white')
        self.plot_line3, = self.axes3.plot([], [], 'g-')

        # # Add main grid to the subplot
        # self.axes3.grid(True, linestyle='-', linewidth=1, alpha=1.0)
        # self.axes3.set_axisbelow(True)
        # self.axes3.grid(color='gray', linestyle='-', linewidth=0.5)
        # self.axes3.set_xticks(range(0, self.plot_window+1, 100))
        # self.axes3.set_yticks(range(0, self.plot_window+1, 100))
        # self.axes3.set_xlim(0, self.plot_window)
        # self.axes3.set_ylim(0, self.plot_window)

        # # Add subgrid to the subplot
        # self.axes3.grid(True, which='minor', linestyle=':', linewidth=0.5)
        # self.axes3.set_xticks(range(0, self.plot_window+1, 10), minor=True)
        # self.axes3.set_yticks(range(0, self.plot_window+1, 10), minor=True)
        # self.axes3.set_xlim(0, self.plot_window)
        # self.axes3.set_ylim(0, self.plot_window)
        
        #add space between plots
        self.figure1.subplots_adjust(hspace=0.5)

        self.startStopButton = QtWidgets.QPushButton("Connect")
        self.startStopButton.setStyleSheet("border: 2px solid black; color: black;")  # Add white border
        self.startStopButton.clicked.connect(self.start_stop_button_clicked)
        self.layout.addWidget(self.startStopButton)
        # Change the color of the axes labels
        self.axes1.xaxis.label.set_color('black')
        self.axes1.yaxis.label.set_color('black')
        self.axes2.xaxis.label.set_color('black')
        self.axes2.yaxis.label.set_color('black')
        self.axes3.xaxis.label.set_color('black')
        self.axes3.yaxis.label.set_color('black')

        # Change the color of the axes lines
        self.axes1.spines['bottom'].set_color('grey')
        self.axes1.spines['top'].set_color('grey')
        self.axes1.spines['left'].set_color('grey')
        self.axes1.spines['right'].set_color('grey')

        self.axes2.spines['bottom'].set_color('grey')
        self.axes2.spines['top'].set_color('grey')
        self.axes2.spines['left'].set_color('grey')
        self.axes2.spines['right'].set_color('grey')

        self.axes3.spines['bottom'].set_color('grey')
        self.axes3.spines['top'].set_color('grey')
        self.axes3.spines['left'].set_color('grey')
        self.axes3.spines['right'].set_color('grey')

        # Change the color of the tick labels on the x and y axes
        self.axes1.tick_params(axis='x', colors='black')
        self.axes1.tick_params(axis='y', colors='black')

        self.axes2.tick_params(axis='x', colors='black')
        self.axes2.tick_params(axis='y', colors='black')

        self.axes3.tick_params(axis='x', colors='black')
        self.axes3.tick_params(axis='y', colors='black')


        self.statusLabel = QtWidgets.QLabel()
        self.statusLabel.setStyleSheet("background-color: white; color: black;")
        self.statusLabel.setText(f"Bluetooth Status: ")
        self.layout.addWidget(self.statusLabel)

        self.timer = QtCore.QTimer()
        self.timer.timeout.connect(self.update_plots)
        self.timer.start(100)

        self.bluetooth_connected = False
        self.loop = asyncio.get_event_loop()
        self.client = None

        self.interval = 0
        self.activity_type = "Unknown"

    @asyncSlot()
    async def start_stop_button_clicked(self):
        if not self.bluetooth_connected:
            self.start_bluetooth()
        else:
            self.stop_bluetooth()

    @asyncSlot()
    async def start_bluetooth(self):
        self.startStopButton.setText("Disconnect")
        self.statusLabel.setText("Bluetooth Status: Connecting...")
        await self.setup_bluetooth()

    @asyncSlot()
    async def stop_bluetooth(self):
        self.bluetooth_connected = False
        self.startStopButton.setText("Connect")
        self.statusLabel.setText("Bluetooth Status: Disconnected")
        if self.client and self.client.is_connected:
            await self.client.stop_notify(heart_rate_characteristic)
            await self.client.stop_notify(IMU_Temp_level_characteristic)
            await self.client.stop_notify(TEMP_BodyPosition_characteristic)
            await self.client.disconnect()

    @asyncSlot()
    async def setup_bluetooth(self):
        devices = await BleakScanner().discover()
        myDevice = next((d for d in devices if d.name == 'Nordic_LBS'), None)

        if myDevice:
            address = myDevice.address
            self.client = BleakClient(address)

            try:
                await self.client.connect()
                if self.client.is_connected:
                    await self.client.start_notify(heart_rate_characteristic, self.notification_handler_1)
                    await self.client.start_notify(IMU_Temp_level_characteristic, self.notification_handler_2)
                    await self.client.start_notify(TEMP_BodyPosition_characteristic, self.notification_handler_3)
                    self.bluetooth_connected = True
                    self.statusLabel.setText("Bluetooth Status: Connected")
                else:
                    raise Exception("Failed to connect")

            except Exception as e:
                print(f"Failed to connect to device at {address}: {e}")
                self.statusLabel.setText("Bluetooth Status: Connection Failed")
                self.stop_bluetooth()
        else:
            print("Device not found")
            self.statusLabel.setText("Bluetooth Status: Device Not Found")
            self.stop_bluetooth()


    async def notification_handler_1(self, sender, ecg_data):
        ecg_values = np.frombuffer(ecg_data, dtype=np.uint16).astype(np.float64)
        ecg_ch1 = ecg_values[::2]
        ecg_ch2 = ecg_values[1::2]
        self.ecg_buffer_ch1[:-len(ecg_ch1)] = self.ecg_buffer_ch1[len(ecg_ch1):]
        self.ecg_buffer_ch1[-len(ecg_ch1):] = ecg_ch1
        self.ecg_buffer_ch2[:-len(ecg_ch2)] = self.ecg_buffer_ch2[len(ecg_ch2):]
        self.ecg_buffer_ch2[-len(ecg_ch2):] = ecg_ch2

        self.interval = self.interval + 1
        self.ecg_buffer2_ch1[:-len(ecg_ch1)] = self.ecg_buffer2_ch1[len(ecg_ch1):]
        self.ecg_buffer2_ch1[-len(ecg_ch1):] = ecg_ch1
        if self.ecg_buffer2_ch1[0] != 0:
            if self.interval % 125 == 0:
                pulse = self.get_heart_rate(self.ecg_buffer2_ch1)
                #print(pulse)
                
                if self.activity_type == "Resting" and pulse < 100:
                    self.pulse_ch1.setStyleSheet("background-color: white; color: black;")
                elif self.activity_type == "Resting" and pulse < 120:
                    self.pulse_ch1.setStyleSheet("background-color: white; color: yellow;")
                elif self.activity_type == "Resting" and pulse > 120:
                    self.pulse_ch1.setStyleSheet("background-color: white; color: red;")
                else:
                    self.pulse_ch1.setStyleSheet("background-color: white; color: black;")
                
                self.pulse_ch1.setText(f"Pulse channel 1: {round(pulse, 1)} bpm")
                self.pulse_ch1.setAlignment(QtCore.Qt.AlignCenter)  # Set alignment to center
                
        self.ecg_buffer2_ch2[:-len(ecg_ch2)] = self.ecg_buffer2_ch2[len(ecg_ch2):]
        self.ecg_buffer2_ch2[-len(ecg_ch2):] = ecg_ch2
        if self.ecg_buffer2_ch2[0] != 0:
            if self.interval % 125 == 0:
                pulse2 = self.get_heart_rate(self.ecg_buffer2_ch2)
                print(f"activity_type: {self.activity_type}")
                
                if self.activity_type == "Resting" and pulse2 < 100:
                    self.pulse_ch2.setStyleSheet("background-color: white; color: black;")
                elif self.activity_type == "Resting" and pulse2 < 120:
                    self.pulse_ch2.setStyleSheet("background-color: white; color: yellow;")
                elif self.activity_type == "Resting" and pulse2 > 120:
                    self.pulse_ch2.setStyleSheet("background-color: white; color: red;")
                else:
                    self.pulse_ch2.setStyleSheet("background-color: white; color: black;")
                

                self.pulse_ch2.setText(f"Pulse channel 2: {round(pulse2, 1)} bpm")
                self.pulse_ch2.setAlignment(QtCore.Qt.AlignCenter)  # Set alignment to center
                self.interval = 0

                
    
    async def notification_handler_2(self, sender, imu_data):
        # Multiply by 2 because of the range of IMU and divide by 10e5 to get the correct value and subtract offset
        imu_values = (2 * np.frombuffer(imu_data, dtype=np.uint32) / 10e5) - 0.25

        # Update the imu_buffer with imu_values
        self.imu_buffer[:-len(imu_values)] = self.imu_buffer[4:]  # Shift the buffer to the left, excluding the last four positions
        self.imu_buffer[-len(imu_values):] = imu_values  # Assign the imu_values to the last four positions of the buffer
        
        self.activity_type = self.process_IMU_data(self.imu_buffer[-activityLevel_threshold:])
        
        # Update the activity label with the current activity type
        self.activityLabel.setText(f"Activity-Type: {self.activity_type}")
        self.activityLabel.setAlignment(QtCore.Qt.AlignCenter)  # Set alignment to center

        self.sample_rate_counter += 1
    
    async def notification_handler_3(self, sender, TempPos_data):
        TempPos_values = np.frombuffer(TempPos_data, dtype=np.uint16)
        self.tempLabel.setText(f"Body-Temperature: {TempPos_values[0]/100} °C")
        body_position = self.determine_Body_position(TempPos_values[1])
        self.posLabel.setText(f"Body-Position: {body_position}")

    def process_IMU_data(self, IMU_values):
        peaks, _ = find_peaks(IMU_values, height=2*5.6)
        num_peaks = len(peaks)
        if num_peaks < 5:
            return "Resting"
        else:
            peaks_run, _ = find_peaks(IMU_values, height=2*10)
            if peaks_run > 5:
                return "Running"
            else:
                return "Walking"
    
    def get_heart_rate(self, ecg_values):
        out = ecg.ecg(signal=ecg_values, sampling_rate=100, show=False)
        heart_rate = out['heart_rate']
        heart_rate_sum = np.sum(heart_rate)
        counter = len(heart_rate)

        average_heart_rate = heart_rate_sum / counter
        counter = 0
        heart_rate_sum = 0
        return average_heart_rate
            
    def determine_Body_position(self, posLabel):
        if posLabel == 1:
            return "Upright position"
        elif posLabel == 2:
            return "Handstand"
        elif posLabel == 3:
            return "Lying sideways on the left"
        elif posLabel == 4:
            return "Lyin sideways on the right"
        elif posLabel == 5:
            return "Lying on the back"
        elif posLabel == 6:
            return "Lying on the stomach"
        else:
            return "Unclear Position"

    def update_plots(self):
        if np.any(self.imu_buffer != 0):
            x = np.arange(self.sample_index, self.sample_index + self.plot_window)
            y1 = self.ecg_buffer_ch1[-self.plot_window:]
            y2 = self.ecg_buffer_ch2[-self.plot_window:]
            y3 = self.imu_buffer[-self.plot_window:]

            self.plot_line1.set_data(x, y1)
            self.axes1.relim()
            self.axes1.autoscale_view()


            self.plot_line2.set_data(x, y2)
            self.axes2.relim()
            self.axes2.autoscale_view()


            self.plot_line3.set_data(x, y3)
            self.axes3.relim()
            self.axes3.autoscale_view()
            
            self.canvas1.draw()
            self.canvas2.draw()
            self.canvas3.draw()
            #print(f"Samples per refresh: {self.sample_rate_counter}")
            #print(f"Incomming Data Frequncy: {self.sample_rate_counter/0.1}")            
            self.sample_rate_counter = 0

if __name__ == "__main__":
    app = QtWidgets.QApplication([])
    window = MainWindow()
    window.show()

    with loop:
        loop.run_forever()