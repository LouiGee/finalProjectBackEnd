package com.example.AuthMicroservice.AuthMicroservice.InitialUsers;


import com.example.AuthMicroservice.AuthMicroservice.Domain.Role;
import com.example.AuthMicroservice.AuthMicroservice.Domain.User;
import com.example.AuthMicroservice.AuthMicroservice.Repositories.RoleRepository;
import com.example.AuthMicroservice.AuthMicroservice.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;


@Configuration
@RequiredArgsConstructor
public class InitialUsers {

    @Bean
    public CommandLineRunner runnerUser(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        return args -> {

            roleRepository.save(Role.builder().name("Production Analyst").build());
            roleRepository.save(Role.builder().name("Production Manager").build());

            var paRoles = roleRepository.findByName("Production Analyst");
            var pmRoles = roleRepository.findByName("Production Manager");

            User productionAnalyst = new User(
                    "John",
                    "Smith",
                    "JohnProductionAnalyst@cookfood.com",
                    passwordEncoder.encode("Analyst12345"),
                    true,
                    false,
                    List.of(paRoles)

            );

            User productionManager = new User(
                    "Sarah",
                    "Jones",
                    "SarahProductionManager@cookfood.com",
                    passwordEncoder.encode("Manager12345"),
                    true,
                    false,
                    List.of(pmRoles)

            );

            userRepository.saveAll(List.of(productionAnalyst, productionManager));



        };

    }
}
