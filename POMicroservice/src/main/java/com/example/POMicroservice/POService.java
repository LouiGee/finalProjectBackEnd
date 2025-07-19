
package com.example.POMicroservice;

import com.example.POMicroservice.DTO.POItemNumber;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class POService {

    @Autowired
    private final PORepository poRepository;

    public POService(PORepository poRepository) {
        this.poRepository = poRepository;
    }



    public List<PO> getAllNonApprovedPO() {
        return poRepository.findByStatus("awaiting-approval");
    }

    public List<PO> getAllApprovedPO() {
        return poRepository.findByStatus("approved");
    }

    @Transactional
    public void approvePOs(List<POItemNumber> POItemNumbers, String email) {

        //Update Status, Approved By, Date Approved
        poRepository.approvePOsByItemNumbers(POItemNumbers, email, LocalDateTime.now());

}}

