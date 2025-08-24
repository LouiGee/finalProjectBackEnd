package com.example.POMicroservice.Pact.consumer;

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

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "POTempProvider")
public class POTempConsumerPactTest {

    @Pact(provider = "POTempProvider", consumer = "POTempConsumer")
    public V4Pact getAllPOTemp(PactDslWithProvider builder) {
        return builder
                .given("Temp POs exist")
                .uponReceiving("A request for all Temp POs")
                .path("/api/potemp/all")
                .method("GET")
                .willRespondWith()
                .status(200)
                .body(LambdaDsl.newJsonArrayMinLike(6,array -> {
                    array.object(poTemp -> {
                        poTemp.stringType("ponumber");           // Any string accepted
                        poTemp.stringType("poitemnumber");
                        poTemp.stringType("company");
                        poTemp.stringType("item");
                        poTemp.stringType("unit");
                        poTemp.numberType("quantity");
                        poTemp.numberType("price");
                    });
                }).build())
                .toPact(V4Pact.class);
    }



    @Test
    @PactTestFor(pactMethod = "getAllPOTemp")
    void testGetAllTempPO(MockServer mockServer) throws Exception {
        String baseUrl = mockServer.getUrl();

        HttpResponse<String> response = Unirest.get(baseUrl + "/api/potemp/all").asString();

        assertEquals(200, response.getStatus());

        String responseBody = response.getBody();
        assertNotNull(responseBody);

        // Simple check: response is a JSON array
        JSONArray poArray = new JSONArray(responseBody);
        assertFalse(poArray.isEmpty(), "poTemp list should not be empty");


        // Check the first object has expected keys (not specific values)
        JSONObject firstPO = poArray.getJSONObject(0);
        assertTrue(firstPO.has("ponumber"));
        assertTrue(firstPO.has("company"));
        assertTrue(firstPO.has("price"));
    }

    @Pact(provider = "POTempProvider", consumer = "POTempConsumer")
    public V4Pact pactForSubmitTempPO(PactDslWithProvider builder) {
        return builder
                .given("There are temporary POs ready to submit")
                .uponReceiving("A POST request to submit temporary POs")
                .path("/api/potemp/submit")
                .method("POST")
                .willRespondWith()
                .status(200)
                .body(LambdaDsl.newJsonArrayMinLike(3, array -> {
                    array.object(po -> {
                        po.stringType("ponumber");
                        po.stringType("poitemnumber");
                        po.stringType("company");
                        po.stringType("item");
                        po.stringType("unit");
                        po.numberType("quantity");
                        po.numberType("price");
                    });
                }).build())
                .toPact(V4Pact.class);
    }

    @Test
    @PactTestFor(pactMethod = "pactForSubmitTempPO")
    void testSubmitTempPO(MockServer mockServer) {
        String url = mockServer.getUrl() + "/api/potemp/submit";

        HttpResponse<String> response = Unirest.post(url)
                .header("Content-Type", "application/json")
                .body("") // no request body expected
                .asString();

        assertEquals(200, response.getStatus());

        JSONArray responseArray = new JSONArray(response.getBody());
        assertFalse(responseArray.isEmpty());

        JSONObject first = responseArray.getJSONObject(0);
        assertTrue(first.has("ponumber"));
        assertTrue(first.has("poitemnumber"));
        assertTrue(first.has("company"));
        assertTrue(first.has("item"));
        assertTrue(first.has("unit"));
        assertTrue(first.has("quantity"));
        assertTrue(first.has("price"));
    }


    @Pact(provider = "POTempProvider", consumer = "POTempConsumer")
    public V4Pact pactForDeleteTempPO(PactDslWithProvider builder) {
        return builder
                .given("A temp PO exists to delete")
                .uponReceiving("A DELETE request to delete temp POs")
                .path("/api/potemp/delete")
                .method("DELETE")
                .headers("Content-Type", "application/json")
                .body(LambdaDsl.newJsonArrayMinLike(1, array -> {
                    array.object(o -> o.stringType("poitemnumber", "PO01-01"));
                }).build())
                .willRespondWith()
                .status(200)
                .toPact(V4Pact.class);
    }

    @Test
    @PactTestFor(pactMethod = "pactForDeleteTempPO")
    void testDeleteTempPO(MockServer mockServer) {
        String url = mockServer.getUrl() + "/api/potemp/delete";

        String requestBody = "[{\"poitemnumber\": \"PO01-01\"}]";

        HttpResponse<String> response = Unirest.delete(url)
                .header("Content-Type", "application/json")
                .body(requestBody)
                .asString();

        assertEquals(200, response.getStatus());
    }

    @Pact(provider = "POTempProvider", consumer = "POTempConsumer")
    public V4Pact createPOTempPact(PactDslWithProvider builder) {
        return builder
                .given("A new POTemp can be created without returning body")
                .uponReceiving("A POST request to add a POTemp")
                .path("/api/potemp/add")
                .method("POST")
                .headers("Content-Type", "application/json")
                .body(LambdaDsl.newJsonBody(body -> {
                    body.stringType("ponumber", "PO1");
                    body.stringType("poitemnumber", "PO1-01");
                    body.stringType("company", "CookFood Ltd");
                    body.stringType("item", "Tomatoes");
                    body.stringType("unit", "kg");
                    body.numberType("quantity", 100);
                    body.numberType("price", 2.5);
                }).build())
                .willRespondWith()
                .status(200)
                .toPact(V4Pact.class);
    }

    @Test
    @PactTestFor(pactMethod = "createPOTempPact")
    void testCreatePOTemp(MockServer mockServer) {
        String url = mockServer.getUrl() + "/api/potemp/add";

        String requestBody = """
            {
              "ponumber": "PO1",
              "poitemnumber": "PO-01",
              "company": "CookFood Ltd",
              "item": "Tomatoes",
              "unit": "kg",
              "quantity": 100,
              "price": 2.5
            }
            """;

        HttpResponse<String> response = Unirest.post(url)
                .header("Content-Type", "application/json")
                .body(requestBody)
                .asString();

        assertEquals(200, response.getStatus());
    }

    @Pact(provider = "POTempProvider", consumer = "POTempConsumer")
    public V4Pact updatePOTempPact(PactDslWithProvider builder) {
        return builder
                .given("A POTemp exists to be updated")
                .uponReceiving("A PUT request to update a POTemp field")
                .path("/api/potemp/edit/PO02-04") // This is PO02-04 for a reason - leave as is
                .method("PUT")
                .headers("Content-Type", "application/json")
                .body(LambdaDsl.newJsonBody(body -> {
                    body.stringType("field", "quantity");
                    body.numberType("newValue", 50);  // Object can be number, string, etc.
                }).build())
                .willRespondWith()
                .status(200)
                .toPact(V4Pact.class);
    }

    @Test
    @PactTestFor(pactMethod = "updatePOTempPact")
    void testUpdatePOTemp(MockServer mockServer) {
        String url = mockServer.getUrl() + "/api/potemp/edit/PO02-04";

        String requestBody = """
            {
              "field": "quantity",
              "newValue": 50
            }
            """;

        HttpResponse<String> response = Unirest.put(url)
                .header("Content-Type", "application/json")
                .body(requestBody)
                .asString();

        assertEquals(200, response.getStatus());
    }

}


