/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.zxing.spring.boot;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link ZxingAutoConfiguration}.
 *
 * <p>Verifies bean registration and the {@code ApplicationContextAware} wiring.</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@DisplayName("ZxingAutoConfiguration Tests")
class ZxingAutoConfigurationTest {

    private final ApplicationContextRunner runner = new ApplicationContextRunner();

    @Test
    @DisplayName("Auto-configuration can be instantiated directly")
    void testInstantiation() {
        ZxingAutoConfiguration configuration = new ZxingAutoConfiguration();
        assertThat(configuration).isNotNull();
    }

    @Test
    @DisplayName("Auto-configuration registers all template beans and binds the context")
    void testRegistersBeans() {
        runner.withUserConfiguration(ZxingAutoConfiguration.class)
                .run(context -> {
                    assertThat(context).hasSingleBean(ZxingAutoConfiguration.class);
                    assertThat(context).hasSingleBean(ZxingAztecCodeTemplate.class);
                    assertThat(context).hasSingleBean(ZxingBarCodeTemplate.class);
                    assertThat(context).hasSingleBean(ZxingQrCodeTemplate.class);
                    assertThat(context).hasSingleBean(ZxingProperties.class);

                    ZxingAutoConfiguration configuration = context.getBean(ZxingAutoConfiguration.class);
                    ApplicationContext ctx = configuration.getApplicationContext();
                    // setApplicationContext wires the context (a non-Assertable delegate)
                    assertThat(ctx).isNotNull();
                });
    }

    @Test
    @DisplayName("Bean methods return distinct template instances")
    void testBeanMethods() {
        ZxingAutoConfiguration configuration = new ZxingAutoConfiguration();
        assertThat(configuration.aztecCodeTemplate()).isInstanceOf(ZxingAztecCodeTemplate.class);
        assertThat(configuration.barCodeTemplate()).isInstanceOf(ZxingBarCodeTemplate.class);
        assertThat(configuration.qrcodeTemplate()).isInstanceOf(ZxingQrCodeTemplate.class);
    }
}
