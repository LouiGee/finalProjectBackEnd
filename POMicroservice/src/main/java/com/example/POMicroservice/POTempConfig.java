
package com.example.POMicroservice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class POTempConfig {

    @Bean
    CommandLineRunner commandLineRunner(POTempService service) {

        return args -> {

            PO PO1 = new PO("1","Company1", "Item", "Kg", 20, 10.00,"Not-submitted");
            PO PO2 = new PO("1","Company2", "Item", "Kg", 20, 10.00, "Not-submitted");
            PO PO3 = new PO("1","Company3", "Item", "Kg", 20, 10.00, "Not-submitted");

            POTemp PO4 = new POTemp("1","Company4", "Item", "Kg", 20, 10.00, "Awaiting-approval");
            POTemp PO5 = new POTemp("1","Company5", "Item", "Kg", 20, 10.00, "Awaiting-approval");
            POTemp PO6 = new POTemp("1","Company6", "Item", "Kg", 20, 10.00, "Awaiting-approval");

            service.createPOTemp(PO1);
            service.createPOTemp(PO2);
            service.createPOTemp(PO3);

            service.CopyTempPOBasket();

            service.createPOTemp(PO4);
            service.createPOTemp(PO5);
            service.createPOTemp(PO6);

        };

    }
}
