
package com.example.POMicroservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public PO getPOById(String PONumber) {
        return poRepository.findById(PONumber).orElse(null);
    }

    public PO createPO(PO po) {

        return poRepository.save(po);

    }

    public PO updatePO(PO po) {return poRepository.save(po);}
}

