
package com.example.POMicroservice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class POConfig {

    @Bean
    CommandLineRunner commandLineRunner(POService service) {

        return args -> {

            PO PO1 = new PO("1","Company1", "Item", "Kg", 20, 10.00,"Not-submitted");
            PO PO2 = new PO("1","Company2", "Item", "Kg", 20, 10.00, "Not-submitted");
            PO PO3 = new PO("1","Company3", "Item", "Kg", 20, 10.00, "Not-submitted");

            PO PO4 = new PO("1","Company4", "Item", "Kg", 20, 10.00, "Awaiting-approval");
            PO PO5 = new PO("1","Company5", "Item", "Kg", 20, 10.00, "Awaiting-approval");
            PO PO6 = new PO("1","Company6", "Item", "Kg", 20, 10.00, "Awaiting-approval");

            service.createPO(PO1);
            service.createPO(PO2);
            service.createPO(PO3);

            service.createPO(PO4);
            service.createPO(PO5);
            service.createPO(PO6);

        };

    }
}
