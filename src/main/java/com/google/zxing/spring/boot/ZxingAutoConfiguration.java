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
public class ZxingAutoConfiguration implements ApplicationContextAware {

	private ApplicationContext applicationContext;
 
	@Bean
	public ZxingAztecCodeTemplate aztecCodeTemplate() {
		return new ZxingAztecCodeTemplate();
	}
	
	@Bean
	public ZxingBarCodeTemplate barCodeTemplate() {
		return new ZxingBarCodeTemplate();
	}
	
	@Bean
	public ZxingQrCodeTemplate qrcodeTemplate() {
		return new ZxingQrCodeTemplate();
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

}
