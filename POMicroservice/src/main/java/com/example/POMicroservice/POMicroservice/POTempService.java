package com.example.POMicroservice.POMicroservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class POTempService {

    @Autowired
    private final POTempRepository POTempRepository;

    @Autowired
    private final PORepository PORepository;


    public POTempService(PORepository PORepository, POTempRepository POTempRepository ) {
        this.PORepository = PORepository;
        this.POTempRepository = POTempRepository; ;
    }


    public List<POTemp> getAllTempPO() {
        return POTempRepository.findAll();
    }

    public POTemp createPOTemp(POTemp po) {

        String PONumber = generatePONumber();

        String POItemNumber = generatePOItemNumber();

        po.setPonumber(PONumber);

        po.setPoitemnumber(POItemNumber);

        po.setDateRaised(LocalDateTime.now());

        return POTempRepository.save(po);

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

    public void CopyTempPOBasket() {

        List<POTemp> poTempsList = POTempRepository.findAll();

        List<PO> poList = poTempsList.stream()
                .map(tempPO -> {
                    PO po = new PO();
                    po.setPoitemnumber(tempPO.getPoitemnumber());
                    po.setPonumber(tempPO.getPonumber());
                    po.setQuantity(tempPO.getQuantity());
                    po.setPrice(tempPO.getPrice());
                    po.setUserId(tempPO.getUserId());
                    po.setCompany(tempPO.getCompany());
                    po.setUnit(tempPO.getUnit());
                    po.setDateRaised(tempPO.getDateRaised());
                    po.setItem(tempPO.getItem());
                    // map other fields as needed
                    return po;
                })
                .collect(Collectors.toList());

        PORepository.saveAll(poList);

        POTempRepository.deleteAll();

    }


    public void deleteTempPO(String POItemNumber) {

        // Extract numeric part (last 2 digits) of the PO item number
        int deletedItemIndex = Integer.parseInt(POItemNumber.substring(POItemNumber.length() - 2));

        // Delete the specified PO entry
        POTempRepository.deleteById(POItemNumber);

        // Get all remaining PO entries
        List<POTemp> poTempsList = POTempRepository.findAll();

        // Delete Temp Table

        POTempRepository.deleteAll();

        // Filter entries that had a higher index than the deleted one
        List<POTemp> poFilteredTempList = poTempsList.stream()
                .filter(tempPO -> {
                    String itemNumber = tempPO.getPoitemnumber();
                    int numberPart = Integer.parseInt(itemNumber.substring(itemNumber.length() - 2));
                    return numberPart > deletedItemIndex;
                })
                .toList();

        // Re-index the filtered entries
        for (POTemp tempPO : poFilteredTempList) {
            String itemNumber = tempPO.getPoitemnumber();
            String prefix = itemNumber.substring(0, itemNumber.length() - 2);

            int numberPart = Integer.parseInt(itemNumber.substring(itemNumber.length() - 2));
            numberPart--; // Shift down

            // Format as two digits with leading zero if needed
            String newItemNumber = prefix + String.format("%02d", numberPart);

            tempPO.setPoitemnumber(newItemNumber);
            POTempRepository.save(tempPO);
        }
    }


    public PO updatePO(PO po) {return PORepository.save(po);}
}

