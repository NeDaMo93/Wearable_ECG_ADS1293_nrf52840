#include <zephyr/kernel.h>
#include <zephyr/device.h>
#include <zephyr/devicetree.h>
#include <zephyr/drivers/gpio.h>
#include <hal/nrf_gpio.h>
#include <zephyr/drivers/adc.h>
#include <zephyr/logging/log.h>
#include <stdio.h>

#define LED_BLUE NRF_GPIO_PIN_MAP(1,10)
#define LED_RED NRF_GPIO_PIN_MAP(1,15)



int ntc; 						// NTC-Resistor of Varta Easypack S
float batt_temp;
bool alarm, batt, temp, volt;

/************************************** BATTERY VOLTAGE MESSUREMENT ***********************************************/
#define BATT_ADC_NODE DT_NODELABEL(adc)
static const struct device *adc_dev = DEVICE_DT_GET(BATT_ADC_NODE);

#define ADC_RESOLUTION			10
#define BATT_ADC_CHANNEL		5
#define BATT_ADC_PORT			SAADC_CH_PSELP_PSELP_AnalogInput5
#define ADC_REFERENCE			ADC_REF_INTERNAL
#define ADC_GAIN				ADC_GAIN_1_6

struct adc_channel_cfg batt_chl_cfg = {
    .channel_id = BATT_ADC_CHANNEL,
    .gain = ADC_GAIN,
    .reference = ADC_REFERENCE,
#ifdef CONFIG_ADC_NRFX_SAADC
	.input_positive = BATT_ADC_PORT
#endif
};

int16_t batt_buffer[1];

const struct adc_sequence batt_sequence = {
	.channels    = BIT(BATT_ADC_CHANNEL),
	.buffer      = &batt_buffer,
	.buffer_size = sizeof(batt_buffer),
	.resolution  = ADC_RESOLUTION,
};

/************************************** BATTERY TEMPERATURE MESSUREMENT ***********************************************/
#define NTC_ADC_NODE DT_NODELABEL(adc)
static const struct device *adc_dev1 = DEVICE_DT_GET(NTC_ADC_NODE);

#define NTC_ADC_CHANNEL		0
#define NTC_ADC_PORT		SAADC_CH_PSELP_PSELP_AnalogInput0


struct adc_channel_cfg ntc_chl_cfg = {
    .channel_id = NTC_ADC_CHANNEL,
    .gain = ADC_GAIN,
    .reference = ADC_REFERENCE,
#ifdef CONFIG_ADC_NRFX_SAADC
	.input_positive = NTC_ADC_PORT
#endif
};

int16_t ntc_buffer[1];

const struct adc_sequence ntc_sequence = {
	.channels    = BIT(NTC_ADC_CHANNEL),
	.buffer      = &ntc_buffer,
	.buffer_size = sizeof(ntc_buffer),
	.resolution  = ADC_RESOLUTION,
};

void battery_management_initialization()
{
	nrf_gpio_cfg_output(LED_BLUE);
	nrf_gpio_cfg_output(LED_RED);



/******** BATTERY VOLTAGE MESSUREMENT ***************************************************/
	int err_batt;
	if (!device_is_ready(adc_dev)){
		printk("adc_dev not ready!\n");
		return;
	}

	err_batt = adc_channel_setup(adc_dev, &batt_chl_cfg);
	if (err_batt != 0) {
		printk("Setting up of the ADC channel failed with code %d", err_batt);
		return;
	}

/******* BATTERY TEMPERATURE MESSUREMENT ***********************************************/
	int err_ntc;
	if (!device_is_ready(adc_dev1)){
		printk("adc_dev not ready!\n");
		return;
	}

	err_ntc = adc_channel_setup(adc_dev1, &ntc_chl_cfg);
	if (err_ntc != 0) {
		printk("Setting up of the ADC channel failed with code %d", err_ntc);
		return;
	}
	printk("Battery initialized successful\n");
}

void get_battery_info()
{	
	int err_batt;
	int err_ntc;
	err_batt = adc_read(adc_dev, &batt_sequence);
	if (err_batt != 0) {
		printk("BatterieVoltage reading failed with code %d", err_batt);
		return;
	}

	err_ntc = adc_read(adc_dev1, &ntc_sequence);
	if (err_ntc != 0) {
		printk("Temperature reading failed with code %d", err_ntc);
		return;
	}
	int32_t mv_ntc = 0;
	int32_t mv_batt;
	int32_t adc_vref;
	int32_t adc_vref1;
	uint16_t count = 0;

	adc_vref = adc_ref_internal(adc_dev);
	adc_vref1 = adc_ref_internal(adc_dev1);


		mv_batt = batt_buffer[0];
		adc_raw_to_millivolts(adc_vref, ADC_GAIN, ADC_RESOLUTION, &mv_batt);

		mv_batt = mv_batt * 2 - 40;

		mv_ntc = ntc_buffer[0]; 
		adc_raw_to_millivolts(adc_vref1, ADC_GAIN, ADC_RESOLUTION, &mv_ntc);
	
		// nicht zwingend notwendig!!
		// if (mv_batt > 3460){
		// 	if (mv_ntc != 3260){
		// 		ntc = 40 + 750*mv_ntc/(3260-mv_ntc);
		// 	} else {
		// 		ntc = 40 + 750*mv_ntc;
		// 	}
		// 	//batt_temp = ((mv_ntc-(3260 / 1.264))/(-29.04));
		// } else{
		// 	if (mv_ntc != (mv_batt-200)){
		// 		ntc = 40 + 750*mv_ntc/((mv_batt-200)-mv_ntc);
		// 	} else {
		// 		ntc = 40 + 750*mv_ntc;
		// 	}
		// 	batt_temp = ((mv_ntc-((mv_batt-200) / 1.264))/(-29.04));
		// }

		volt = (mv_batt < 3700);
		batt = (mv_ntc < 3100);		// checking if Battery is inserted

		//if (count == 4){
			char buffer[10];
			int len = snprintf(buffer, sizeof(buffer), "%.1f", batt_temp);
			//printk("%d)", count)
			if (batt){
				// printk("Temp: %s;	mv_ntc: %d	ntc: %dOhm;	mv_batt: %d\n", buffer, mv_ntc, ntc, mv_batt);
			} else {
				printk("no Battery insert!	");
				//printk("Temp: %s;	ntv_mV: %d	ntc: %dOhm;	Volt: %d", buffer, mv_ntc, ntc, mv_batt);
				printk("\n");
			}


		temp = ((mv_ntc >= 2219 || mv_ntc <= 1261));
		alarm = ((mv_ntc > 2507 || mv_ntc < 1159 || mv_batt < 3500));




	if (!volt && !temp && !alarm){
		nrf_gpio_pin_toggle(LED_BLUE);
		k_msleep(100);
		nrf_gpio_pin_toggle(LED_BLUE);
		k_msleep(100);
		nrf_gpio_pin_toggle(LED_BLUE);
		nrf_gpio_pin_clear(LED_RED);
	} else if ((volt || temp) && !alarm){
		nrf_gpio_pin_clear(LED_BLUE);
		nrf_gpio_pin_toggle(LED_RED);
	} else if (alarm){
		nrf_gpio_pin_set(LED_RED);
		nrf_gpio_pin_clear(LED_BLUE);
	}

	nrf_gpio_pin_clear(LED_BLUE);
	if (!alarm){
		nrf_gpio_pin_clear(LED_RED);
	}

}


