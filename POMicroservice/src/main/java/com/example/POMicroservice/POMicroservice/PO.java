

package com.example.POMicroservice.POMicroservice;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;


@Getter
@Setter
@Entity
@Table(name = "purchaseOrder")
public class PO {

    private String UserId;
    private String ponumber;

    @Id
    private String poitemnumber;

    private String Company;
    private String Item;
    private String Unit;
    private int Quantity;
    private double Price;

    @CreatedDate
    @Column(nullable = true, updatable = false)
    private LocalDateTime dateRaised;

    public PO(String UserId, String PONumber, String POItemNumber, String Company, String Item, String Unit, int Quantity , double Price, LocalDateTime dateRaised ) {

        this.UserId = UserId;
        this.ponumber = PONumber;
        this.poitemnumber = POItemNumber;
        this.Company = Company;
        this.Item = Item;
        this.Unit = Unit;
        this.Quantity = Quantity;
        this.Price = Price;
        this.dateRaised = dateRaised;

    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getUserId() {
        return UserId;
    }

    public String getPonumber() {
        return ponumber;
    }

    public String getPoitemnumber() {
        return poitemnumber;
    }

    public PO() {

    }

    public void setPonumber(String ponumber) {
        this.ponumber = ponumber;
    }

    public void setPoitemnumber(String poitemnumber) {
        this.poitemnumber = poitemnumber;
    }

    public void setCompany(String company) {
        Company = company;
    }

    public void setItem(String item) {
        Item = item;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public void setDateRaised(LocalDateTime dateRaised) {
        this.dateRaised = dateRaised;
    }

    public LocalDateTime getDateRaised() {
        return dateRaised;
    }

    public double getPrice() {
        return Price;
    }

    public int getQuantity() {
        return Quantity;
    }

    public String getUnit() {
        return Unit;
    }

    public String getItem() {
        return Item;
    }

    public String getCompany() {
        return Company;
    }


}


