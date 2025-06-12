package com.example.ratifyBackend.POMicroService;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "purchase_order_temp")
public class POTemp {

    @Id
    private String ponumber;

    private String poitemnumber;

    private String Company;
    private String Item;
    private String Unit;
    private int Quantity;
    private double Price;

    @CreatedDate
    @Column(nullable = true, updatable = false)
    private LocalDateTime dateRaised;

    public POTemp(String Company, String Item, String Unit, int Quantity, double Price, LocalDateTime dateRaised) {

        this.Company = Company;
        this.Item = Item;
        this.Unit = Unit;
        this.Quantity = Quantity;
        this.Price = Price;
        this.dateRaised = dateRaised;

    }

    public POTemp() {}


    public String getPONumber() {
        return ponumber;
    }

    public String getPOItemNumber() {
        return poitemnumber;
    }

    public String getCompany() {
        return Company;
    }

    public String getItem() {
        return Item;
    }

    public String getUnit() {
        return Unit;
    }

    public int getQuantity() {
        return Quantity;
    }

    public double getPrice() {
        return Price;
    }

    public LocalDateTime getDateRaised() {
        return dateRaised;
    }

    public void setPONumber(String PONumber) {
        this.ponumber = PONumber;
    }

    public void setPOItemNumber(String POItemNumber) {
        this.poitemnumber = POItemNumber;
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
}

