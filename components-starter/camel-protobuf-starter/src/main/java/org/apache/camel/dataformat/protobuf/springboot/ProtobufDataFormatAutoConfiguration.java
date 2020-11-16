/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.dataformat.protobuf.springboot;

import javax.annotation.Generated;
import org.apache.camel.CamelContext;
import org.apache.camel.dataformat.protobuf.ProtobufDataFormat;
import org.apache.camel.spi.DataFormat;
import org.apache.camel.spi.DataFormatCustomizer;
import org.apache.camel.spring.boot.CamelAutoConfiguration;
import org.apache.camel.spring.boot.DataFormatConfigurationProperties;
import org.apache.camel.spring.boot.util.CamelPropertiesHelper;
import org.apache.camel.spring.boot.util.ConditionalOnCamelContextAndAutoConfigurationBeans;
import org.apache.camel.spring.boot.util.ConditionalOnHierarchicalProperties;
import org.apache.camel.spring.boot.util.HierarchicalPropertiesEvaluator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * Generated by camel-package-maven-plugin - do not edit this file!
 */
@Generated("org.apache.camel.springboot.maven.SpringBootAutoConfigurationMojo")
@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(CamelAutoConfiguration.class)
@Conditional(ConditionalOnCamelContextAndAutoConfigurationBeans.class)
@EnableConfigurationProperties({DataFormatConfigurationProperties.class,ProtobufDataFormatConfiguration.class})
@ConditionalOnHierarchicalProperties({"camel.dataformat", "camel.dataformat.protobuf"})
public class ProtobufDataFormatAutoConfiguration {

    @Autowired
    private ApplicationContext applicationContext;
    private final CamelContext camelContext;
    @Autowired
    private ProtobufDataFormatConfiguration configuration;

    public ProtobufDataFormatAutoConfiguration(
            org.apache.camel.CamelContext camelContext) {
        this.camelContext = camelContext;
    }

    @Lazy
    @Bean
    public DataFormatCustomizer configureProtobufDataFormatFactory() {
        return new DataFormatCustomizer() {
            @Override
            public void configure(String name, DataFormat target) {
                CamelPropertiesHelper.copyProperties(camelContext, configuration, target);
            }
            @Override
            public boolean isEnabled(String name, DataFormat target) {
                return HierarchicalPropertiesEvaluator.evaluate(
                        applicationContext,
                        "camel.dataformat.customizer",
                        "camel.dataformat.protobuf.customizer")
                    && target instanceof ProtobufDataFormat;
            }
        };
    }
}