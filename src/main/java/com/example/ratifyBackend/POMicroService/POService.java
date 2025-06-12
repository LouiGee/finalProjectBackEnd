
package com.example.ratifyBackend.POMicroService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class POService {

    private final PORepository PORepository;

    @Autowired

    public POService(PORepository poRepository) {
        this.PORepository = poRepository;
    }

    public List<PO> getAllPO() {
        return PORepository.findAll();
    }

    public PO getPOById(String PONumber) {
        return PORepository.findById(PONumber).orElse(null);
    }

    public PO createPO(PO po) {

        return PORepository.save(po);

    }

    public PO updatePO(PO po) {return PORepository.save(po);}
}

