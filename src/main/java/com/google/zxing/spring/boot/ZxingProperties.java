package com.google.zxing.spring.boot;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(ZxingProperties.PREFIX)
/**\n * Auto-configuration for ZxingProperties.\n *\n * @author <a href="https://github.com/loong10k">Loong Wan</a>\n * @since 1.0.0\n */
public class ZxingProperties {

	public static final String PREFIX = "zxing";


}
