package com.google.zxing.spring.boot;

import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.zxing.common.BitMatrix;
 
@Configuration
@ConditionalOnClass(BitMatrix.class)
@EnableConfigurationProperties({ ZxingProperties.class })
/**\n * Auto-configuration for ZxingAutoConfiguration.\n *\n * @author <a href="https://github.com/loong10k">Loong Wan</a>\n * @since 1.0.0\n */
public class ZxingAutoConfiguration implements ApplicationContextAware {

	private ApplicationContext applicationContext;
 
	@Bean
	/**
	 * aztec Code Template.
	 *
	 * @return the result
	 */
	public ZxingAztecCodeTemplate aztecCodeTemplate() {
		return new ZxingAztecCodeTemplate();
	}
	
	@Bean
	/**
	 * bar Code Template.
	 *
	 * @return the result
	 */
	public ZxingBarCodeTemplate barCodeTemplate() {
		return new ZxingBarCodeTemplate();
	}
	
	@Bean
	/**
	 * qrcode Template.
	 *
	 * @return the result
	 */
	public ZxingQrCodeTemplate qrcodeTemplate() {
		return new ZxingQrCodeTemplate();
	}
	
	@Override
	/**
	 * Sets the application context.
	 *
	 * @param applicationContext the application context
	 * @throws BeansException if an error occurs
	 */
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	/**
	 * Returns the application context.
	 *
	 * @return the application context
	 */
	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

}
