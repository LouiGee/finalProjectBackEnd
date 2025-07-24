package com.example.POMicroservice.pact.consumer;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.LambdaDsl;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.V4Pact;
import au.com.dius.pact.core.model.annotations.Pact;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "POProvider", port = "8081")
public class POConsumerPactTest {

    @Pact(consumer = "POConsumer")
    public V4Pact getAllPO(PactDslWithProvider builder) {
        return builder
                .given("POs exist")
                .uponReceiving("A request for all POs")
                .path("/api/po/all")
                .method("GET")
                .willRespondWith()
                .status(200)
                .body(LambdaDsl.newJsonArrayMinLike(6,array -> {
                    array.object(po -> {
                        po.stringType("ponumber");           // Any string accepted
                        po.stringType("poitemnumber");
                        po.stringType("company");
                        po.stringType("item");
                        po.stringType("unit");
                        po.numberType("quantity");
                        po.numberType("price");
                        po.stringType("status");

                        // Date strings - you can use a regex matcher to validate ISO-8601 date format:
                        po.stringMatcher("dateRaised", "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(\\.\\d{1,6})?");

                        po.stringType("raisedBy");
                        // Accepts empty string or date-time format
                    });
                }).build())
                .toPact(V4Pact.class);
    }

    /*


    @Pact(consumer = "POConsumer")
    public V4Pact getAllNonApprovedPO(PactDslWithProvider builder) {
        return builder
                .given("Non Approved POs exist")
                .uponReceiving("A request for all Non Approved POs")
                .path("/api/po/allNonApprovedPo")
                .method("GET")
                .willRespondWith()
                .status(200)
                .body(LambdaDsl.newJsonArrayMinLike(3,array -> {
                    array.object(po -> {
                        po.stringType("ponumber");           // Any string accepted
                        po.stringType("poitemnumber");
                        po.stringType("company");
                        po.stringType("item");
                        po.stringType("unit");
                        po.numberType("quantity");
                        po.numberType("price");
                        po.stringType("status", "Awaiting-Approval" );

                        // Date strings - you can use a regex matcher to validate ISO-8601 date format:
                        po.stringMatcher("dateRaised", "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(\\.\\d{1,6})?");

                        po.stringType("raisedBy");
                        // Accepts empty string or date-time format
                    });
                }).build())
                .toPact(V4Pact.class);
    }

*/


    @Test
    void testGetAllPO(MockServer mockServer) throws Exception {
        String baseUrl = mockServer.getUrl();

        HttpResponse<String> response = Unirest.get(baseUrl + "/api/po/all").asString();

        assertEquals(200, response.getStatus());

        String responseBody = response.getBody();
        assertNotNull(responseBody);

        // Simple check: response is a JSON array
        JSONArray poArray = new JSONArray(responseBody);
        assertTrue(poArray.length() > 0, "PO list should not be empty");

        // Check the first object has expected keys (not specific values)
        JSONObject firstPO = poArray.getJSONObject(0);
        assertTrue(firstPO.has("ponumber"));
        assertTrue(firstPO.has("company"));
        assertTrue(firstPO.has("dateRaised"));
        assertTrue(firstPO.has("price"));
    }
}