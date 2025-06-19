package com.example.POMicroservice.POMicroservice;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/api/po")
public class POController {

    @Autowired
    private POService poService;

    @GetMapping("/all")
    public ResponseEntity<List<PO>> getAllPo() {
        List<PO> po = poService.getAllPO();
        return new ResponseEntity<>(po, HttpStatus.OK);
    }

}