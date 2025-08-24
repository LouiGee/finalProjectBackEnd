
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

            POTemp PO1 = new POTemp("JohnDepartmentAnalyst@cookfood.com","Acme Fruits/Veg", "Tomatoes (Loose)", "Kg", 5, 10.00, "Expected for 01/08/2025 Batch Cook" );
            POTemp PO2 = new POTemp("JohnDepartmentAnalyst@cookfood.com","Acme Fruits/Veg", "Celery", "Packs 500g", 20, 15.24, "Expected for 01/08/2025 Batch Cook" );
            POTemp PO3 = new POTemp("JohnDepartmentAnalyst@cookfood.com","Acme Fruits/Veg", "Potatoes", "Kg", 10, 12.54, "Expected for 01/08/2025 Batch Cook" );

            POTemp PO4 = new POTemp("JohnDepartmentAnalyst@cookfood.com","Kent Seasonings", "Tumeric", "Kg", 2, 16.74, "Usual bi-monthly stock up" );
            POTemp PO5 = new POTemp("JohnDepartmentAnalyst@cookfood.com","Kent Seasonings", "Garlic Powder", "Kg", 9, 25.74,"Usual bi-monthly stock up");
            POTemp PO6 = new POTemp("JohnDepartmentAnalyst@cookfood.com","Kent Seasonings", "Cumin", "Kg", 7, 21.34, "Usual bi-monthly stock up");

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
