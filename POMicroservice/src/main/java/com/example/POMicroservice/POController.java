package com.example.POMicroservice;

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
@RequestMapping("/api/po")
public class POController {

    @Autowired
    private POService poService;

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

        poService.approvePOs(poItemNumberList, email);
        return new ResponseEntity<>(poItemNumberList, HttpStatus.OK);
    }

}