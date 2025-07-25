package com.example.POMicroservice.Repositories;

import com.example.POMicroservice.Domain.PO;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface PORepository extends JpaRepository<PO, String> {

    Optional<PO> findTopByOrderByPonumberDesc();

    List<PO> findByStatus(String status);

    @Modifying
    @Query("UPDATE PO p SET p.status = 'Approved', p.approvedBy = :approvedBy, p.dateApproved = :approvalDate WHERE p.poitemnumber = :poItemNumber")
    void approvePOsByItemNumbers(@Param("poItemNumber") String poItemNumber,
                                 @Param("approvedBy") String approvedBy,
                                 @Param("approvalDate") LocalDateTime approvalDate);

}

