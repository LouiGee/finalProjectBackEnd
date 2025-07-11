package com.example.POMicroservice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
    public interface POTempRepository extends JpaRepository<POTemp, String> {

        Optional<POTemp> findByPoitemnumber(String poitemnumber);

        Optional<POTemp> findTopByOrderByPoitemnumberDesc();

        Optional<POTemp> findTopByOrderByPonumberDesc();

        void deleteByPoitemnumber(String poitemnumber);

        @Query("SELECT Count(*) FROM POTemp")
        int POTempCount();

}
