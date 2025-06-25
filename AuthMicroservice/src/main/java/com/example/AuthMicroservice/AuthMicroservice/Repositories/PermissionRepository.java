package com.example.AuthMicroservice.AuthMicroservice.Repositories;

import com.example.AuthMicroservice.AuthMicroservice.Domain.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission,Integer> {

    Permission findByName(String name);

}