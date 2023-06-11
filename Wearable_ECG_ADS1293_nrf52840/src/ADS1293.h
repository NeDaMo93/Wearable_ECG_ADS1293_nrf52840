#include <zephyr/kernel.h>
#include <zephyr/drivers/spi.h>
#include <zephyr/drivers/gpio.h>        
#include <stdio.h>     

#define SPI1_NODE                   DT_NODELABEL(spi1)
#define DATA_READY_NODE 	        DT_NODELABEL(data_ready)  // Data ready node

#define SPI_FREQ 1000000
#define SPI_SLAVE 0
#define SPI_WORD_SIZE 8
static volatile bool ads1293_interrupt = false;

typedef struct {
    uint32_t channel_1_ecgVal;
    uint32_t channel_2_ecgVal;
} ECG_Values;

int ads1293_spi_transfer(const struct device *spi_dev, uint8_t addr, bool is_read, uint8_t data);
int ads1293_write_register(const struct device *spi_dev, uint8_t addr, uint8_t data);
int ads1293_read_register(const struct device *spi_dev, uint8_t addr);
int ads1293_init(struct device *spi_dev);
void ads1293_SPI_init(void);
void GPIOinterrupt_init(void);
void ecg_data_callback(const struct device *dev, struct gpio_callback *cb, uint32_t pins);
void get_register_val(struct device *spi_dev);
ECG_Values read_ecg_values(void);
