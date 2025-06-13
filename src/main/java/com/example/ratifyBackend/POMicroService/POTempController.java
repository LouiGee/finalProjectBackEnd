package com.example.ratifyBackend.POMicroService;

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

    @GetMapping("/all")
    public ResponseEntity<List<POTemp>> getAllPoTemp() {
        List<POTemp> poTemp = poTempService.getAllTempPO();
        return new ResponseEntity<>(poTemp, HttpStatus.OK);

    }

}