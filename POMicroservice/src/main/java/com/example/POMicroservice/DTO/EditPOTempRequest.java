package com.example.POMicroservice.DTO;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditPOTempRequest {

    String field;
    Object newValue;

}
