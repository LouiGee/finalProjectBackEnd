package com.example.POMicroservice.POMicroservice;

import com.example.POMicroservice.POMicroservice.APIValidation.AuthApiService;
import com.example.POMicroservice.POMicroservice.DTO.POItemNumber;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/api/temppo")
public class POTempController {

    @Autowired
    private POTempService poTempService;



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
            poTempService.deleteTempPO(poItemNumberString);

        }

        return HttpStatus.OK;

    }

    @PostMapping("/add")
    public ResponseEntity<POTemp> createPOTemp(@RequestBody POTemp poTemp) {
        POTemp newPOTemp = poTempService.createPOTemp(poTemp);
        return new ResponseEntity<>(newPOTemp, HttpStatus.CREATED);
    }


}