
package com.example.ratifyBackend.POMicroService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class POTempConfig {

    @Bean
    CommandLineRunner commandLineRunner(POTempService service) {

        return args -> {

            POTemp PO1 = new POTemp("Company", "Item", "Kg", 20, 10.00, LocalDateTime.now());
            POTemp PO2 = new POTemp("Company", "Item", "Kg", 20, 10.00, LocalDateTime.now());


            service.createTempPO(PO1);
            service.createTempPO(PO2);
        };


    }
}
