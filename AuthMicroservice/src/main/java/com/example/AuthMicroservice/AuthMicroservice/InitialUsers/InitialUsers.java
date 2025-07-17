package com.example.AuthMicroservice.AuthMicroservice.InitialUsers;


import com.example.AuthMicroservice.AuthMicroservice.Domain.Permission;
import com.example.AuthMicroservice.AuthMicroservice.Domain.User;
import com.example.AuthMicroservice.AuthMicroservice.Repositories.PermissionRepository;
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
    public CommandLineRunner runnerUser(UserRepository userRepository, PermissionRepository permissionRepository, PasswordEncoder passwordEncoder, PermissionRepository permissionsRepository) {
        return args -> {

            permissionRepository.save(Permission.builder().name("Department Analyst").build());
            permissionRepository.save(Permission.builder().name("Department Manager").build());

            var daRole = permissionRepository.findByName("Department Analyst");
            var dmRole = permissionRepository.findByName("Department Manager");

            User productionAnalyst = new User(
                    "John",
                    "Smith",
                    "JohnDepartmentAnalyst@cookfood.com",
                    passwordEncoder.encode("Analyst12345"),
                    true,
                    false,
                    daRole

            );

            User productionManager = new User(
                    "Sarah",
                    "Jones",
                    "SarahDepartmentManager@cookfood.com",
                    passwordEncoder.encode("Manager12345"),
                    true,
                    false,
                    dmRole

            );

            userRepository.saveAll(List.of(productionAnalyst, productionManager));



        };

    }
}
