
package com.example.POMicroservice.InitialPOs;

import com.example.POMicroservice.Domain.POTemp;
import com.example.POMicroservice.Services.POTempService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class POTempConfig {

    @Bean
    CommandLineRunner commandLineRunner(POTempService service) {

        return args -> {

            POTemp PO1 = new POTemp("JohnDepartmentAnalyst@cookfood.com","Company1", "Item", "Kg", 20, 10.00);
            POTemp PO2 = new POTemp("JohnDepartmentAnalyst@cookfood.com","Company2", "Item", "Kg", 20, 10.00);
            POTemp PO3 = new POTemp("JohnDepartmentAnalyst@cookfood.com","Company3", "Item", "Kg", 20, 10.00);

            POTemp PO4 = new POTemp("JohnDepartmentAnalyst@cookfood.com","Company4", "Item", "Kg", 20, 10.00);
            POTemp PO5 = new POTemp("JohnDepartmentAnalyst@cookfood.com","Company5", "Item", "Kg", 20, 10.00);
            POTemp PO6 = new POTemp("JohnDepartmentAnalyst@cookfood.com","Company6", "Item", "Kg", 20, 10.00);

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
