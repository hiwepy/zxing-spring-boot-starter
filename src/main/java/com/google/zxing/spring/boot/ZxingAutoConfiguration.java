package com.google.zxing.spring.boot;

import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
 

@Configuration
@ConditionalOnProperty(prefix = ZxingProperties.PREFIX, value = "enabled", havingValue = "true")
@EnableConfigurationProperties({ ZxingProperties.class })
public class ZxingAutoConfiguration implements ApplicationContextAware {

	private ApplicationContext applicationContext;
 
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

}
