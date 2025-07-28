
package com.example.POMicroservice.Services;

import com.example.POMicroservice.DTO.POItemNumber;
import com.example.POMicroservice.Domain.PO;
import com.example.POMicroservice.Repositories.PORepository;
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

    public List<PO> getAllPO() {
        return poRepository.findAll();
    }

    public List<PO> getAllNonApprovedPO() {
        return poRepository.findByStatus("awaiting-approval");
    }

    public List<PO> getAllApprovedPO() {
        return poRepository.findByStatus("approved");
    }

    public List<PO> getAllPaidPO() {
        return poRepository.findByStatus("paid");
    }

    @Transactional
    public void approvePOs(List<POItemNumber> POItemNumbers, String email) {

        for (POItemNumber poItemNumber : POItemNumbers) {
            //Update Status, Approved By, Date Approved
            System.out.println(poItemNumber.getPoitemnumber());
            poRepository.approvePOsByItemNumbers(poItemNumber.getPoitemnumber(), email, LocalDateTime.now());


        }

    }

    @Transactional
    public void payPOs(List<POItemNumber> POItemNumbers, String email) {

        for (POItemNumber poItemNumber : POItemNumbers) {
            //Update Status, Approved By, Date Approved
            System.out.println(poItemNumber.getPoitemnumber());
            poRepository.payPOsByItemNumbers(poItemNumber.getPoitemnumber(), email, LocalDateTime.now());


        }

    }


    public List<Object> getSummaryStatistics() {

        return poRepository.getSummaryStatistics();
    }


}

