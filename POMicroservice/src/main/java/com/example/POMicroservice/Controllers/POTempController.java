package com.example.POMicroservice.Controllers;

import com.example.POMicroservice.DTO.EditPOTempRequest;
import com.example.POMicroservice.DTO.POItemNumber;
import com.example.POMicroservice.Domain.POTemp;
import com.example.POMicroservice.Repositories.POTempRepository;
import com.example.POMicroservice.Services.POTempService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/api/potemp")
public class POTempController {

    @Autowired
    private POTempService poTempService;


    @GetMapping("/all")
    public ResponseEntity<List<POTemp>> getAllPOTemp() {
        List<POTemp> poTemp = poTempService.getAllPOTemp();
        return new ResponseEntity<>(poTemp, HttpStatus.OK);

    }

    @PostMapping("/submit")
    public ResponseEntity<List<POTemp>> submitPOTemp() {
        List<POTemp> poTemp = poTempService.getAllPOTemp();
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
    public HttpStatus createPOTemp(@RequestBody POTemp poTemp) {
        poTempService.createPOTemp(poTemp);
        return HttpStatus.CREATED;
    }

    @PutMapping("/edit/{poItemNumber}")
    public HttpStatus updatePOTemp(@RequestBody EditPOTempRequest request
                                             , @PathVariable("poItemNumber") String poItemNumber) {
        poTempService.updatePOTemp(request, poItemNumber);

        return HttpStatus.OK;
    }


}