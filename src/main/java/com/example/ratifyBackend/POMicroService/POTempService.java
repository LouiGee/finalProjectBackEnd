package com.example.ratifyBackend.POMicroService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class POTempService {


    private final POTempRepository POTempRepository;

    private final PORepository PORepository;

    @Autowired
    public POTempService(PORepository PORepository, POTempRepository POTempRepository ) {
        this.PORepository = PORepository;
        this.POTempRepository = POTempRepository; ;
    }


    public List<POTemp> getAllTempPO() {
        return POTempRepository.findAll();
    }

    public POTemp createTempPO(POTemp po) {

        String PONumber = generatePONumber();

        String POItemNumber = generatePOItemNumber();

        po.setPONumber(PONumber);
        po.setPOItemNumber(POItemNumber);

        return POTempRepository.save(po);

    }


    private String generatePONumber() {

        Optional<PO> mostRecentPO = PORepository.findTopByOrderByPonumberDesc();
        String lastPONumber = mostRecentPO.map(PO::getPONumber).orElse("PO00");

        if (lastPONumber.equals("P000")) {
            return "PO01";
        }

        else if (!lastPONumber.equals("P000") && POTempRepository.POTempCount() == 0) {

            String lastPONumberString = String.valueOf(lastPONumber);
            int number = Integer.parseInt(lastPONumberString.substring(2));
            return String.format("PO%02d", number + 1);
        }

        else if (!lastPONumber.equals("P000") && POTempRepository.POTempCount() > 0) {
            return lastPONumber;  }


        return lastPONumber;
    }


    private String generatePOItemNumber() {

         // Custom query
        Optional<PO> mostRecentPO = PORepository.findTopByOrderByPonumberDesc();
        String lastPONumber = mostRecentPO.map(PO::getPONumber).orElse("Empty");
        

        if (lastPONumber.equals("Empty")) {
            return "PO01A";
        }

        else if (POTempRepository.POTempCount() > 0) {

            Optional<POTemp> mostRecentTempPO = POTempRepository.findTopByOrderByPoitemnumberDesc();
            String lastPOTempNumber = mostRecentTempPO.map(POTemp::getPOItemNumber).orElse("Empty");

            //will not be empty

            char lastChar = lastPOTempNumber.charAt(lastPOTempNumber.length() - 1);
            char nextChar = (char)(lastChar + 1);

            return lastPOTempNumber.substring(0, lastPOTempNumber.length() - 1) + nextChar;

        }

        else if (POTempRepository.POTempCount() == 0) {

            String lastPONumberString = lastPONumber;
            int number = Integer.parseInt(lastPONumberString.substring(2));
            return String.format("PO%02d", number + 1) + "A" ;

        }

        return lastPONumber;
    }

    public void deletePO(String PONumber) {
        PORepository.deleteById(PONumber);
    }

    public PO updatePO(PO po) {return PORepository.save(po);}
}

