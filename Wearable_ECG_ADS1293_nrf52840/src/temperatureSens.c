#include <zephyr/kernel.h>
#include <zephyr/device.h>
#include <zephyr/devicetree.h>
#include <zephyr/drivers/gpio.h>
#include <hal/nrf_gpio.h>
#include <zephyr/drivers/adc.h>
#include <zephyr/logging/log.h>
#include <stdio.h>


float bodytemp;
#define Power NRF_GPIO_PIN_MAP(0,04)

#define ADC_NODE DT_NODELABEL(adc)
static const struct device *adc_dev = DEVICE_DT_GET(ADC_NODE);

#define ADC_RESOLUTION	10
#define ADC_CHANNEL		1
#define ADC_PORT		SAADC_CH_PSELP_PSELP_AnalogInput1
#define ADC_REFERENCE	ADC_REF_INTERNAL
#define ADC_GAIN		ADC_GAIN_1_6

struct adc_channel_cfg chl1_cfg = {
    .channel_id = ADC_CHANNEL,
    .gain = ADC_GAIN,
    .reference = ADC_REFERENCE,
#ifdef CONFIG_ADC_NRFX_SAADC
	.input_positive = ADC_PORT
#endif
};

int16_t sample_buffer[1];

const struct adc_sequence sequence = {
	.channels    = BIT(ADC_CHANNEL),
	.buffer      = &sample_buffer,
	.buffer_size = sizeof(sample_buffer),
	.resolution  = ADC_RESOLUTION,
};

void temp_sens_initialization()
{
	nrf_gpio_cfg_output(Power);

	int err;
	if (!device_is_ready(adc_dev)){
		printk("adc_dev not ready!\n");
		return;
	}

	err = adc_channel_setup(adc_dev, &chl1_cfg);
	if (err != 0) {
		printk("Setting up of the ADC channel failed with code %d", err);
		return;
	}
    else{
        printk("Temperature sensor initialization successfully \n");
    }
}

float get_bodyTemperature()
{
    int count = 0;
    float zwischenwert= 0;
    float bodytemp;
    nrf_gpio_pin_set(Power);

    while(count < 500)
    { 
        int err;
        err = adc_read(adc_dev, &sequence);
        if (err != 0) {
            printk("Temperature reading failed with code %d", err);
            return 0;
        }
        int32_t mv_value = sample_buffer[0];
        int32_t adc_vref = adc_ref_internal(adc_dev);
    
        adc_raw_to_millivolts(adc_vref, ADC_GAIN, ADC_RESOLUTION, &mv_value);

        zwischenwert = zwischenwert + mv_value;
        bodytemp = -28.5 + (zwischenwert*100)/(count*1168);
        
        count++;
    }
    nrf_gpio_pin_clear(Power);
    return bodytemp;
}
