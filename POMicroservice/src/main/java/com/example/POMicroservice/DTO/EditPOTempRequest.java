package com.example.POMicroservice.DTO;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditPOTempRequest {

    String field;
    Object newValue;


    public EditPOTempRequest() {
    }

    public EditPOTempRequest(String field, Object newValue) {
        this.field = field;
        this.newValue = newValue;
    }

    public String getField() {
        return field;
    }

    public Object getNewValue() {
        return newValue;
    }
}
