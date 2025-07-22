package com.example.POMicroservice.Controllers;

import com.example.POMicroservice.DTO.POItemNumber;
import com.example.POMicroservice.Domain.PO;
import com.example.POMicroservice.Services.POService;
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
    public ResponseEntity<List<PO>> getAll() {
        List<PO> po = poService.getAllPO();
        return new ResponseEntity<>(po, HttpStatus.OK);
    }

    @GetMapping("/allNonApprovedPo")
    public ResponseEntity<List<PO>> getAllNonApprovedPo() {
        List<PO> po = poService.getAllNonApprovedPO();
        return new ResponseEntity<>(po, HttpStatus.OK);
    }

    @GetMapping("/allApprovedPo")
    public ResponseEntity<List<PO>> getAllApprovedPo() {
        List<PO> po = poService.getAllApprovedPO();
        return new ResponseEntity<>(po, HttpStatus.OK);
    }

    @PostMapping("/approve")
    public ResponseEntity<List<POItemNumber>> approve(@RequestBody List<POItemNumber> poItemNumberList, @CookieValue(name = "email", required = false) String email) {

        for (POItemNumber poItemNumber : poItemNumberList) {System.out.println(poItemNumber.getPoitemnumber());}

        poService.approvePOs(poItemNumberList, email);
        return new ResponseEntity<>(poItemNumberList, HttpStatus.OK);
    }

}