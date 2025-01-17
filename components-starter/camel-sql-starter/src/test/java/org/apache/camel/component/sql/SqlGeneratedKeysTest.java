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
package org.apache.camel.component.sql;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spring.boot.CamelAutoConfiguration;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.annotation.DirtiesContext;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@CamelSpringBootTest
@SpringBootTest(
        classes = {
                CamelAutoConfiguration.class,
                SqlGeneratedKeysTest.class,
                SqlGeneratedKeysTest.TestConfiguration.class,
                BaseSql.TestConfiguration.class
        }
)
public class SqlGeneratedKeysTest extends BaseSql {

    @Test
    @SuppressWarnings("unchecked")
    public void testRetrieveGeneratedKey() {
        // first we create our exchange using the endpoint
        Endpoint endpoint = context.getEndpoint("direct:insert");

        Object body = new Object[] { "project x", "ASF", "new project" };
        Exchange exchange = endpoint.createExchange();
        exchange.getIn().setBody(body);
        exchange.getIn().setHeader(SqlConstants.SQL_RETRIEVE_GENERATED_KEYS, true);

        // now we send the exchange to the endpoint, and receives the response from Camel
        Exchange out = template.send(endpoint, exchange);

        // assertions of the response
        assertNotNull(out);
        assertNotNull(out.getMessage());
        assertNotNull(out.getMessage().getHeader(SqlConstants.SQL_GENERATED_KEYS_DATA));
        assertSame(body, out.getMessage().getBody());

        List<Map<String, Object>> generatedKeys = out.getMessage().getHeader(SqlConstants.SQL_GENERATED_KEYS_DATA, List.class);
        assertNotNull(generatedKeys, "out body could not be converted to a List - was: "
                                     + out.getMessage().getBody());
        assertEquals(1, generatedKeys.get(0).size());

        Map<String, Object> row = generatedKeys.get(0);
        assertEquals(3, row.get("ID"), "auto increment value should be 3");

        assertEquals(1, out.getMessage().getHeader(SqlConstants.SQL_GENERATED_KEYS_ROW_COUNT),
                "generated keys row count should be one");
    }

    // *************************************
    // Config
    // *************************************

    @Configuration
    public static class TestConfiguration {

        @Bean
        public DataSource dataSource() {
            return initDb(EmbeddedDatabaseType.HSQL, "sql/createAndPopulateDatabase3.sql");
        }

        @Bean
        public RouteBuilder routeBuilder() {
            return new RouteBuilder() {
                @Override
                public void configure() throws Exception {
                    from("direct:insert").to("sql:insert into projects (project, license, description) values (#, #, #)");
                }
            };
        }
    }
}
