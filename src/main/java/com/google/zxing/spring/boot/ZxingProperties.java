package com.google.zxing.spring.boot;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(ZxingProperties.PREFIX)
public class ZxingProperties {

	public static final String PREFIX = "spring.zxing";
 
	
}