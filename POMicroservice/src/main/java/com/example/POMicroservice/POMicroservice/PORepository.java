package com.example.POMicroservice.POMicroservice;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface PORepository extends JpaRepository<PO, String> {

    Optional<PO> findTopByOrderByPonumberDesc();

}

