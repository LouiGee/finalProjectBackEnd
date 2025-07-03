
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

    public PO createPOTemp(PO po) {

        String PONumber = generatePONumber();

        String POItemNumber = generatePOItemNumber();

        po.setPonumber(PONumber);

        po.setPoitemnumber(POItemNumber);

        po.setDateRaised(LocalDateTime.now());

        return PORepository.save(po);

    }


    private String generatePONumber() {

        //Check the PO Table
        Optional<PO> mostRecentPO = PORepository.findTopByOrderByPonumberDesc();
        String lastPONumber = mostRecentPO.map(PO::getPonumber).orElse("Empty");

        //Check the Temp PO Table
        Optional<POTemp> mostRecentTempPO = POTempRepository.findTopByOrderByPonumberDesc();
        String lastTempPONumber = mostRecentTempPO.map(POTemp::getPonumber).orElse("Empty");

        // No entries in PO table
        if (lastPONumber.equals("Empty")) {
            return "PO01";
        }

        // entry in PO table but none in Temp
        else if (!lastPONumber.equals("Empty") && POTempRepository.POTempCount() == 0) {

            int number = Integer.parseInt(lastPONumber.substring(2));
            return String.format("PO%02d", number + 1);
        }

        // already entries in Temp table
        else if (POTempRepository.POTempCount() > 0) {
            return lastTempPONumber;  }


        return lastPONumber;
    }


    private String generatePOItemNumber() {

        // Search PO table
        Optional<PO> mostRecentPO = PORepository.findTopByOrderByPonumberDesc();
        String lastPONumber = mostRecentPO.map(PO::getPonumber).orElse("Empty");

        // Search PO Temp table
        Optional<POTemp> mostTempRecentPO = POTempRepository.findTopByOrderByPoitemnumberDesc();
        String lastPOItemNumber = mostTempRecentPO.map(POTemp::getPoitemnumber).orElse("Empty");

        //First Entry
        if (lastPONumber.equals("Empty") && lastPOItemNumber.equals("Empty")) {
            return "PO01-01";
        }

        // Already entries in the temp table
        else if (POTempRepository.POTempCount() > 0) {

            //will not be empty
            int lastNumber = Integer.parseInt(lastPOItemNumber.substring(lastPOItemNumber.length() - 2));
            String nextNumber = String.format("%02d", lastNumber + 1);

            return lastPOItemNumber.substring(0, lastPOItemNumber.length() - 2) + nextNumber;

        }

        // Entries in the PO table but none in the temp table
        else if (!lastPONumber.equals("Empty") && POTempRepository.POTempCount() == 0) {

            int number = Integer.parseInt(lastPONumber.substring(2));
            return String.format("PO%02d", number + 1) + "-01" ;

        }

        return lastPONumber;
    }

    public void updatePOTemp(EditPOTempRequest request, String poItemNumber) {

        //1. Update POTemp

        Optional<POTemp> tempPOToEditOptional = POTempRepository.findByPoitemnumber(poItemNumber);

        System.out.println(tempPOToEditOptional.isPresent());

        System.out.println(request.getField());

        POTemp tempPOToUpdate = tempPOToEditOptional.orElseThrow(() -> new RuntimeException("PO not found"));


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
        POTempRepository.save(tempPOToUpdate);


    }






}

