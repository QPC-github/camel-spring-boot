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
package org.apache.camel.component.validator;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

import org.apache.camel.ValidationException;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.spring.boot.CamelAutoConfiguration;
import org.apache.camel.support.processor.validation.SchemaReader;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;

import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext
@CamelSpringBootTest
@SpringBootTest(
        classes = {
                CamelAutoConfiguration.class,
                ValidatorDtdAccessOffTest.class,
                ValidatorDtdAccessAbstractTest.TestConfiguration.class
        }
)
public class ValidatorDtdAccessOffTest extends ValidatorDtdAccessAbstractTest {

    /** Tests that no external DTD call is executed for StringSource. */
    @Test
    public void testInvalidMessageWithExternalDTDStringSource() throws Exception {
        invalidEndpoint.expectedMessageCount(1);
        finallyEndpoint.expectedMessageCount(1);

        template.sendBody("direct:start", ssrfPayloud);

        MockEndpoint.assertIsSatisfied(validEndpoint, unknownHostExceptionEndpoint, finallyEndpoint);
    }

    /** Tests that external DTD call is not executed for StreamSource. */
    @Test
    public void testInvalidMessageWithExternalDTDStreamSource() throws Exception {
        invalidEndpoint.expectedMessageCount(1);
        finallyEndpoint.expectedMessageCount(1);
        InputStream is = new ByteArrayInputStream(ssrfPayloud.getBytes(StandardCharsets.UTF_8));
        template.sendBody("direct:start", is);

        MockEndpoint.assertIsSatisfied(validEndpoint, unknownHostExceptionEndpoint, finallyEndpoint);
    }

    /** Tests that XXE is not possible for StreamSource. */
    @Test
    public void testInvalidMessageXXESourceStream() throws Exception {
        invalidEndpoint.expectedMessageCount(1);
        finallyEndpoint.expectedMessageCount(1);
        InputStream is = new ByteArrayInputStream(xxePayloud.getBytes(StandardCharsets.UTF_8));
        template.sendBody("direct:start", is);

        MockEndpoint.assertIsSatisfied(validEndpoint, unknownHostExceptionEndpoint, finallyEndpoint);
    }

    @Override
    protected boolean accessExternalDTD() {
        return false;
    }
}
