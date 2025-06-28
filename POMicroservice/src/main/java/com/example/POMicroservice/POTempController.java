package com.example.POMicroservice;

import com.example.POMicroservice.DTO.EditPOTempRequest;
import com.example.POMicroservice.DTO.POItemNumber;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/api/temppo")
public class POTempController {

    @Autowired
    private POTempService poTempService;

    private POTempRepository poTempRepository;


    @GetMapping("/all")
    public ResponseEntity<List<POTemp>> getAllPOTemp() {
        List<POTemp> poTemp = poTempService.getAllTempPO();
        return new ResponseEntity<>(poTemp, HttpStatus.OK);

    }

    @GetMapping("/submit")
    public ResponseEntity<List<POTemp>> submitPOTemp() {
        List<POTemp> poTemp = poTempService.getAllTempPO();
        poTempService.CopyTempPOBasket();
        return new ResponseEntity<>(poTemp, HttpStatus.OK);

    }

    @DeleteMapping("/delete")
    public HttpStatus deletePOTemp(@RequestBody List<POItemNumber> poItemNumberList) {

        for (POItemNumber poItemNumber : poItemNumberList) {

                String poItemNumberString = poItemNumber.getPoitemnumber();
                poTempService.deletePOTemp(poItemNumberString);

        }


        return HttpStatus.OK;

    }

    @PostMapping("/add")
    public ResponseEntity<POTemp> createPOTemp(@RequestBody POTemp poTemp) {
        POTemp newPOTemp = poTempService.createPOTemp(poTemp);
        return new ResponseEntity<>(newPOTemp, HttpStatus.CREATED);
    }

    @PutMapping("/edit/{poItemNumber}")
    public HttpStatus updatePOTemp(@RequestBody EditPOTempRequest request
                                             , @PathVariable("poItemNumber") String poItemNumber) {
        ;

        poTempService.updatePOTemp(request, poItemNumber);

        return HttpStatus.OK;
    }


}