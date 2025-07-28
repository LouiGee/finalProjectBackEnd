package com.example.POMicroservice.Pact.provider;

import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.loader.PactFolder;
import com.example.POMicroservice.DTO.POItemNumber;
import com.example.POMicroservice.Domain.POTemp;
import com.example.POMicroservice.POMicroserviceApplication;
import com.example.POMicroservice.Services.POService;
import com.example.POMicroservice.Services.POTempService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.core5.http.ClassicHttpRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;


import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Provider("POProvider")
@PactFolder("target/pacts")
@ContextConfiguration(classes = {POMicroserviceApplication.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@PactTestFor(hostInterface = "localhost")
public class POProviderPactTest {

    @LocalServerPort
    private int port;

    @Autowired
    private POTempService poTempService;

    @Autowired
    private POService poService;


    private List<String> jwtToken;


    @BeforeEach
    void setupToken() throws Exception {
        jwtToken = fetchJwtToken(); // Call your method to get JWT
    }

    private List<String> fetchJwtToken() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        String loginUrl = "http://localhost:8080/api/auth/login";

        Map<String, String> loginPayload = new HashMap<>();
        loginPayload.put("email", "SarahDepartmentManager@cookfood.com");
        loginPayload.put("password", "Manager12345");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(loginPayload, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(loginUrl, request, String.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return List.of(new ObjectMapper()
                                .readTree(response.getBody())
                                    .get("authenticationToken").asText(), new ObjectMapper()
                                                                                .readTree(response.getBody())
                                                                                    .get("refreshToken").asText() );
        } else {
            throw new RuntimeException("Failed to get JWT: " + response.getStatusCode());
        }
    }


    @BeforeEach
    void before(PactVerificationContext context) {
        context.setTarget(new HttpTestTarget("localhost", port));
    }


    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    void pactVerificationTest(PactVerificationContext context , ClassicHttpRequest request) {

        request.addHeader("Cookie", "authenticationToken=" + jwtToken.getFirst() + "; refreshToken=" + jwtToken.get(1) + "; email=SarahDepartmentManager@cookfood.com");
        context.verifyInteraction();
    }


    @State("POs exist")
    public void setupPOsExist() {
        POTemp PO1 = new POTemp("JohnDepartmentAnalyst@cookfood.com", "Company1", "Item", "Kg", 5, 10.00,"test");
        POTemp PO2 = new POTemp("JohnDepartmentAnalyst@cookfood.com", "Company2", "Item", "Kg", 20, 10.00, "test");
        POTemp PO3 = new POTemp("JohnDepartmentAnalyst@cookfood.com", "Company3", "Item", "Kg", 20, 10.00, "test");
        POTemp PO4 = new POTemp("JohnDepartmentAnalyst@cookfood.com", "Company4", "Item", "Kg", 20, 10.00, "test");
        POTemp PO5 = new POTemp("JohnDepartmentAnalyst@cookfood.com", "Company5", "Item", "Kg", 20, 10.00, "test");
        POTemp PO6 = new POTemp("JohnDepartmentAnalyst@cookfood.com", "Company6", "Item", "Kg", 20, 10.00, "test");

        poTempService.createPOTemp(PO1);
        poTempService.createPOTemp(PO2);
        poTempService.createPOTemp(PO3);
        poTempService.createPOTemp(PO4);
        poTempService.createPOTemp(PO5);
        poTempService.createPOTemp(PO6);
        poTempService.CopyTempPOBasket();

        POItemNumber poIN1 = new POItemNumber(PO1.getPoitemnumber());
        POItemNumber poIN2 = new POItemNumber(PO2.getPoitemnumber());
        POItemNumber poIN3 = new POItemNumber(PO3.getPoitemnumber());


        poService.approvePOs(List.of(poIN1,poIN2, poIN3 ), "SarahDepartmentManager@cookfood.com");

    }


    @State("Non Approved POs exist")
    public void setupNonApprovedPOsExist() {
        POTemp PO1 = new POTemp("JohnDepartmentAnalyst@cookfood.com", "Company1", "Item", "Kg", 5, 10.00,"test");
        POTemp PO2 = new POTemp("JohnDepartmentAnalyst@cookfood.com", "Company2", "Item", "Kg", 20, 10.00, "test");
        POTemp PO3 = new POTemp("JohnDepartmentAnalyst@cookfood.com", "Company3", "Item", "Kg", 20, 10.00, "test");
        POTemp PO4 = new POTemp("JohnDepartmentAnalyst@cookfood.com", "Company4", "Item", "Kg", 20, 10.00, "test");
        POTemp PO5 = new POTemp("JohnDepartmentAnalyst@cookfood.com", "Company5", "Item", "Kg", 20, 10.00, "test");
        POTemp PO6 = new POTemp("JohnDepartmentAnalyst@cookfood.com", "Company6", "Item", "Kg", 20, 10.00, "test");


        poTempService.createPOTemp(PO1);
        poTempService.createPOTemp(PO2);
        poTempService.createPOTemp(PO3);
        poTempService.createPOTemp(PO4);
        poTempService.createPOTemp(PO5);
        poTempService.createPOTemp(PO6);
        poTempService.CopyTempPOBasket();

        POItemNumber poIN1 = new POItemNumber(PO1.getPoitemnumber());
        POItemNumber poIN2 = new POItemNumber(PO2.getPoitemnumber());
        POItemNumber poIN3 = new POItemNumber(PO3.getPoitemnumber());


        poService.approvePOs(List.of(poIN1,poIN2, poIN3 ), "SarahDepartmentManager@cookfood.com");

    }

    @State("Approved POs exist")
    public void setupApprovedPOsExist() {
        POTemp PO1 = new POTemp("JohnDepartmentAnalyst@cookfood.com", "Company1", "Item", "Kg", 5, 10.00,"test");
        POTemp PO2 = new POTemp("JohnDepartmentAnalyst@cookfood.com", "Company2", "Item", "Kg", 20, 10.00, "test");
        POTemp PO3 = new POTemp("JohnDepartmentAnalyst@cookfood.com", "Company3", "Item", "Kg", 20, 10.00, "test");
        POTemp PO4 = new POTemp("JohnDepartmentAnalyst@cookfood.com", "Company4", "Item", "Kg", 20, 10.00, "test");
        POTemp PO5 = new POTemp("JohnDepartmentAnalyst@cookfood.com", "Company5", "Item", "Kg", 20, 10.00, "test");
        POTemp PO6 = new POTemp("JohnDepartmentAnalyst@cookfood.com", "Company6", "Item", "Kg", 20, 10.00, "test");


        poTempService.createPOTemp(PO1);
        poTempService.createPOTemp(PO2);
        poTempService.createPOTemp(PO3);
        poTempService.createPOTemp(PO4);
        poTempService.createPOTemp(PO5);
        poTempService.createPOTemp(PO6);
        poTempService.CopyTempPOBasket();

        POItemNumber poIN1 = new POItemNumber(PO1.getPoitemnumber());
        POItemNumber poIN2 = new POItemNumber(PO2.getPoitemnumber());
        POItemNumber poIN3 = new POItemNumber(PO3.getPoitemnumber());


        poService.approvePOs(List.of(poIN1,poIN2, poIN3 ), "SarahDepartmentManager@cookfood.com");

    }



    @State("PO items to approve exist")
    public void setupPOItemsToApproveExist() {
        POTemp PO1 = new POTemp("JohnDepartmentAnalyst@cookfood.com", "Company1", "Item", "Kg", 5, 10.00,"test");
        POTemp PO2 = new POTemp("JohnDepartmentAnalyst@cookfood.com", "Company2", "Item", "Kg", 20, 10.00, "test");
        POTemp PO3 = new POTemp("JohnDepartmentAnalyst@cookfood.com", "Company3", "Item", "Kg", 20, 10.00, "test");
        POTemp PO4 = new POTemp("JohnDepartmentAnalyst@cookfood.com", "Company4", "Item", "Kg", 20, 10.00, "test");
        POTemp PO5 = new POTemp("JohnDepartmentAnalyst@cookfood.com", "Company5", "Item", "Kg", 20, 10.00, "test");
        POTemp PO6 = new POTemp("JohnDepartmentAnalyst@cookfood.com", "Company6", "Item", "Kg", 20, 10.00, "test");


        poTempService.createPOTemp(PO1);
        poTempService.createPOTemp(PO2);
        poTempService.createPOTemp(PO3);
        poTempService.createPOTemp(PO4);
        poTempService.createPOTemp(PO5);
        poTempService.createPOTemp(PO6);
        poTempService.CopyTempPOBasket();

    }
}