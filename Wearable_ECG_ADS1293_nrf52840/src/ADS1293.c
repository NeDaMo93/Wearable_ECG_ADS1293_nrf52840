#include"ADS1293.h"

//define 3 channels
uint32_t chan1_1 = 0;
uint32_t chan1_2 = 0;
uint32_t chan1_3 = 0;
uint32_t chan2_1 = 0;
uint32_t chan2_2 = 0;
uint32_t chan2_3 = 0;
//define registers
uint8_t reg21;
uint8_t reg22;
uint8_t reg23;

const struct gpio_dt_spec cs_gpio = GPIO_DT_SPEC_GET_OR(SPI1_NODE, cs_gpios, {0});
static const struct device *spi_dev = DEVICE_DT_GET(SPI1_NODE);
static const struct gpio_dt_spec drdy_gpio = GPIO_DT_SPEC_GET(DATA_READY_NODE, gpios);

struct spi_cs_control spi_cs = {
    .gpio = cs_gpio,
    .delay = 2,
};

static const struct spi_config spi_cfg = {
    .frequency = SPI_FREQ,
    .operation = SPI_WORD_SET(SPI_WORD_SIZE) | SPI_OP_MODE_MASTER,
    .slave = SPI_SLAVE,
    .cs = &spi_cs,
};


int ads1293_spi_transfer(const struct device *spi_dev, uint8_t addr, bool is_read, uint8_t data)
{
    uint8_t tx_buf[2] = { (uint8_t)((is_read ? 0x80 : 0x00) | (addr & 0x7F)), data };
    uint8_t rx_buf[2] = { 0 };

    const struct spi_buf tx_bufs[] = {
        {
            .buf = tx_buf,
            .len = sizeof(tx_buf),
        },
    };

    const struct spi_buf rx_bufs[] = {
        {
            .buf = rx_buf,
            .len = sizeof(rx_buf),
        },
    };

    const struct spi_buf_set tx = {
        .buffers = tx_bufs,
        .count = ARRAY_SIZE(tx_bufs),
    };

    const struct spi_buf_set rx = {
        .buffers = rx_bufs,
        .count = ARRAY_SIZE(rx_bufs),
    };

    int err = spi_transceive(spi_dev, &spi_cfg, &tx, &rx);
    if (err != 0) {
        return err;
    }

    return rx_buf[1];
}

int ads1293_write_register(const struct device *spi_dev, uint8_t addr, uint8_t data)
{
    return ads1293_spi_transfer(spi_dev, addr, false, data);
}

int ads1293_read_register(const struct device *spi_dev, uint8_t addr)
{
    return ads1293_spi_transfer(spi_dev, addr, true, 0x00);
}
int ads1293_init(struct device *spi_dev) {
    // Initialize ADS1293 with the provided register settings
    ads1293_write_register(spi_dev, 0x00, 0x00);// stop any conversion.
    ads1293_write_register(spi_dev, 0x01, 0x11);//Connect channel 1’s INP to IN2 and INN to IN1.
    ads1293_write_register(spi_dev, 0x02, 0x19);//Connect channel 2’s INP to IN3 and INN to IN1.
    ads1293_write_register(spi_dev, 0x0A, 0x07);//Enable the common-mode detector on input pins IN1, IN2 and IN3.
    ads1293_write_register(spi_dev, 0x0C, 0x04);// Connect the output of the RLD amplifier internally to pin IN4.
    ads1293_write_register(spi_dev, 0x12, 0x04);// Use external crystal and feed the internal oscillator's output to the digital.
    ads1293_write_register(spi_dev, 0x14, 0x24);// Shuts down unused channel 3’s signal path
    ads1293_write_register(spi_dev, 0x21, 0x08);// Configures the R2 decimation rate as 5 for all channels
    ads1293_write_register(spi_dev, 0x22, 0x20);//Configures the R3 decimation rate as 6 for channel 1.
    ads1293_write_register(spi_dev, 0x23, 0x20);// Configures the R3 decimation rate as 6 for channel 2
    ads1293_write_register(spi_dev, 0x27, 0x08);//Configures the DRDYB source to channel 1 ECG (or fastest channel)
    ads1293_write_register(spi_dev, 0x2F, 0x30);// Enables channel 1 ECG and channel 2 ECG for loop read-back mode
    ads1293_write_register(spi_dev, 0x00, 0x01);// Starts data conversion.
    return 0;
}

void get_register_val(struct device *spi_dev)
{
    // Read back from registers
    reg21 = ads1293_read_register(spi_dev, 0x21);
    reg22 = ads1293_read_register(spi_dev, 0x22);
    reg23 = ads1293_read_register(spi_dev, 0x23);
}



void ads1293_SPI_init(void){
    int ret;

    // Get the SPI device instance
    static struct device *spi_dev = DEVICE_DT_GET(SPI1_NODE);
    
    if (!spi_dev) {
        printk("Failed to get SPI device\n");
        return ;
    }

    // Initialize the ADS1293
    ret = ads1293_init(spi_dev);
    if (ret) {
        printk("Failed to initialize ADS1293\n");
        return;
    } else {
        printk("ADS1293 initialized successfully\n");
        return;
    }
}

ECG_Values read_ecg_values(void) {
    ECG_Values ecg_values;

    chan1_1 = ads1293_read_register(spi_dev, 0x37);
    chan1_2 = ads1293_read_register(spi_dev, 0x38);
    chan1_3 = ads1293_read_register(spi_dev, 0x39);
    ecg_values.channel_1_ecgVal = chan1_1;
    ecg_values.channel_1_ecgVal = (ecg_values.channel_1_ecgVal << 8) | chan1_2;
    ecg_values.channel_1_ecgVal = (ecg_values.channel_1_ecgVal << 8) | chan1_3;

    chan2_1 = ads1293_read_register(spi_dev, 0x3A);
    chan2_2 = ads1293_read_register(spi_dev, 0x3B);
    chan2_3 = ads1293_read_register(spi_dev, 0x3C);
    ecg_values.channel_2_ecgVal = chan2_1;
    ecg_values.channel_2_ecgVal = (ecg_values.channel_2_ecgVal << 8) | chan2_2;
    ecg_values.channel_2_ecgVal = (ecg_values.channel_2_ecgVal << 8) | chan2_3;


    k_sleep(K_MSEC(2));

    return ecg_values;
}


void GPIOinterrupt_init(void)
{

    const struct device *data_ready_dev;
     // Use data_ready.port directly
    data_ready_dev = drdy_gpio.port;
    static struct gpio_callback gpio_cb;

	// Initialize GPIO device and configure for interrupts
    if (!data_ready_dev) {
        printk("GPIO device not ready\n");
        return;
    }
	printk("GPIO device found\n");

	 // Configure reset button pin as input with interrupt on rising edge
    int ret = gpio_pin_configure(data_ready_dev, drdy_gpio.pin, drdy_gpio.dt_flags | GPIO_INPUT);
    if (ret) {
        printk("Failed to configure reset button pin\n");
        return;
    }
	printk("GPIO device configured \n");

	ret = gpio_pin_interrupt_configure(data_ready_dev, drdy_gpio.pin, GPIO_INT_EDGE_TO_ACTIVE);
    if (ret) {
        printk("Failed to configure data_ready interrupt\n");
        return;
    }
    printk("data_ready interrupt configured\n");

	gpio_init_callback(&gpio_cb, ecg_data_callback, BIT(drdy_gpio.pin));
	printk("INIT CALLBACK\n");
	
	ret = gpio_add_callback(data_ready_dev, &gpio_cb);
    if (ret) {
        printk("Failed to add callback\n");
    }
    printk("Callback added \n");
}