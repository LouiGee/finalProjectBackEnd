
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

            POTemp PO1 = new POTemp("1","Company1", "Item", "Kg", 20, 10.00, LocalDateTime.now());
            POTemp PO2 = new POTemp("1","Company2", "Item", "Kg", 20, 10.00, LocalDateTime.now());
            POTemp PO3 = new POTemp("1","Company3", "Item", "Kg", 20, 10.00, LocalDateTime.now());

            POTemp PO4 = new POTemp("1","Company4", "Item", "Kg", 20, 10.00, LocalDateTime.now());
            POTemp PO5 = new POTemp("1","Company5", "Item", "Kg", 20, 10.00, LocalDateTime.now());
            POTemp PO6 = new POTemp("1","Company6", "Item", "Kg", 20, 10.00, LocalDateTime.now());

            service.createTempPO(PO1);
            service.createTempPO(PO2);
            service.createTempPO(PO3);

            service.CopyTempPOBasket();

            service.createTempPO(PO4);
            service.createTempPO(PO5);
            service.createTempPO(PO6);

        };


    }
}
