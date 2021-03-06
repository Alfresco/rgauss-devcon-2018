/*
 * Copyright 2018 Alfresco Software, Ltd.
 *
 * This example is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This example is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this example. If not, see <http://www.gnu.org/licenses/>.
 */
package org.alfresco.example.rgauss.devcon2018;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN;
import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD;
import static org.springframework.http.HttpHeaders.ORIGIN;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.apache.camel.ProducerTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RecognitionTagResultControllerTest
{
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ProducerTemplate producerTemplate;

    private static final String ORIGIN_URL = "http://localhost:4200";

    protected String buildEvent(String eventType, String nodeId)
    {
        return "{"
                + "\"@class\":\"org.alfresco.events.types.NodeContentPutEvent\""
                + ",\"id\":\"3ea23b92-b1a1-4eed-b9fa-a85faa63822d\""
                + ",\"type\":\"" + eventType + "\""
                + ",\"username\":\"admin\""
                + ",\"timestamp\":1515203787325"
                + ",\"seqNumber\":1"
                + ",\"txnId\":\"e1459b0c-a9ca-430b-ae55-b5d4f7abd2d0\""
                + ",\"networkId\":\"\""
                + ",\"client\":null"
                + ",\"nodeId\":\"" + nodeId + "\""
                + ",\"siteId\":\"swsdp\""
                + ",\"nodeType\":\"cm:content\""
                + ",\"name\":\"testJPEG.jpg\""
                + ",\"nodeModificationTime\":1515203787238"
                + ",\"paths\":[\"java.util.ArrayList\",[\"/Company Home/Sites/swsdp/documentLibrary/testJPEG.jpg\"]]"
                + ",\"parentNodeIds\":[\"java.util.ArrayList\",[[\"java.util.ArrayList\",[\"8f2105b4-daaf-4874-9e8a-2152569d109b\",\"b4cff62a-664d-4d45-9302-98723eac1319\",\"28dbb8d8-9f3d-4b23-91f5-2ad3bab89a1e\",\"0bb7e4e8-5978-4ec4-bfa7-f63b41a8c263\",\"5df7c3f5-2256-4b8e-ab28-c46839390cce\"]]]]"
                + ",\"aspects\":[\"java.util.HashSet\",[\"sys:localized\",\"sys:referenceable\",\"cm:auditable\"]]"
                + ",\"nodeProperties\":{\"@class\":\"java.util.HashMap\",\"NODE_IS_CLASSIFIED\":false}"
                + ",\"size\":10289"
                + ",\"mimeType\":\"image/jpeg\""
                + ",\"encoding\":\"UTF-8\""
                + "}\n";
    }

    protected void testApi(String eventType, String nodeId, String expectedTag) throws Exception
    {
        String event = buildEvent(eventType, nodeId);
        // Inject an event
        producerTemplate.sendBody(TestEventConsumptionRoute.ENDPOINT_TEST_EVENT_FROM, event);

        // Retrieve the results
        mvc.perform(MockMvcRequestBuilders.get("/recognition-results").accept(MediaType.APPLICATION_JSON)
                .header(ACCESS_CONTROL_REQUEST_METHOD, GET).header(ORIGIN, ORIGIN_URL)).andExpect(status().isOk())
                .andExpect(content().string(containsString(expectedTag)))
                .andExpect(header().string(ACCESS_CONTROL_ALLOW_ORIGIN, ORIGIN_URL));
    }

    @Test
    public void testApiWithClasspathNodeId() throws Exception
    {
        testApi("CONTENTPUT", "classpath:testJPEG.jpg", "horizontal bar");
    }

    @Test
    public void testApiWithRepositoryNodeId() throws Exception
    {
        testApi("CONTENTPUT", "7bb9c846-fcc5-43b5-a893-39e46ebe94d4", "pill bottle");
    }
}
