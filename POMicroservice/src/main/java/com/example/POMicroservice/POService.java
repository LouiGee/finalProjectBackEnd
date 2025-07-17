
package com.example.POMicroservice;

import com.example.POMicroservice.DTO.EditPOTempRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class POService {


    private final PORepository poRepository;

    public List<PO> getAllPO() {
        return poRepository.findAll();
    }


    public PO createPO(PO po) {

        String PONumber = generatePONumber(po);

        String POItemNumber = generatePOItemNumber();

        po.setPonumber(PONumber);

        po.setPoitemnumber(POItemNumber);

        po.setDateRaised(LocalDateTime.now());

        return poRepository.save(po);

    }


    private String generatePONumber(PO po) {

        //Check the PO Table
        Optional<PO> mostRecentPOByUser = poRepository.findTopByUserIDOrderByPonumberDesc(po.getUserId());
        String lastPONumber = mostRecentPOByUser.map(PO::getPonumber).orElse("Empty");

        // No entries in PO table
        if (lastPONumber.equals("Empty")) {
            return "PO01";
        }

        // If last entry by a user in the PO table is "not submitted" then return last entries PO number
        if (!lastPONumber.equals("Empty") && po.getUserId() == mostRecentPO.get().getUserId()) {


            return;
        }


        else if (poRepository.POCount() == 0) {

            int number = Integer.parseInt(lastPONumber.substring(2));
            return String.format("PO%02d", number + 1);
        }

        // already entries in Temp table
        else if (poRepository.POCount() > 0) {
            return lastTempPONumber;  }


        return lastPONumber;
    }


    private String generatePOItemNumber() {

        // Search PO table
        Optional<PO> mostRecentPO = poRepository.findTopByOrderByPonumberDesc();
        String lastPONumber = mostRecentPO.map(PO::getPonumber).orElse("Empty");

        // Search PO Temp table
        Optional<PO> mostTempRecentPO = poRepository.findTopByOrderByPoitemnumberDesc();
        String lastPOItemNumber = mostTempRecentPO.map(PO::getPoitemnumber).orElse("Empty");

        //First Entry
        if (lastPONumber.equals("Empty") && lastPOItemNumber.equals("Empty")) {
            return "PO01-01";
        }

        // Already entries in the temp table
        else if (poRepository.POCount() > 0) {

            //will not be empty
            int lastNumber = Integer.parseInt(lastPOItemNumber.substring(lastPOItemNumber.length() - 2));
            String nextNumber = String.format("%02d", lastNumber + 1);

            return lastPOItemNumber.substring(0, lastPOItemNumber.length() - 2) + nextNumber;

        }

        // Entries in the PO table but none in the temp table
        else if (!lastPONumber.equals("Empty") && poRepository.POCount() == 0) {

            int number = Integer.parseInt(lastPONumber.substring(2));
            return String.format("PO%02d", number + 1) + "-01" ;

        }

        return lastPONumber;
    }

    public void updatePOTemp(EditPOTempRequest request, String poItemNumber) {

        //1. Update POTemp

        Optional<PO> tempPOToEditOptional = poRepository.findByPoitemnumber(poItemNumber);

        // DEBUG System.out.println(tempPOToEditOptional.isPresent());

        // DEBUG System.out.println(request.getField());

        PO tempPOToUpdate = tempPOToEditOptional.orElseThrow(() -> new RuntimeException("PO not found"));

        if (request.getField().equals("company")) {

            tempPOToUpdate.setCompany((String) request.getNewValue());

        }
        else if (request.getField().equals("item")) {

            tempPOToUpdate.setItem((String) request.getNewValue());

        }

        else if (request.getField().equals("quantity")) {

            tempPOToUpdate.setQuantity((Integer) request.getNewValue());

        }

        else if (request.getField().equals("price")) {

            tempPOToUpdate.setPrice((Double) request.getNewValue());

        }

        // Save edited TempPO
        poRepository.save(tempPOToUpdate);


    }






}

