package com.example.POMicroservice;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface PORepository extends JpaRepository<PO, String> {


    Optional<PO> findByPoitemnumber(String poitemnumber);

    Optional<PO> findTopByOrderByPoitemnumberDesc();

    Optional<PO> findTopByUserIDOrderByPonumberDesc(String userID);

    void deleteByPoitemnumber(String poitemnumber);



    @Query("SELECT Count(*) FROM POTemp")
    int POCount();


}

