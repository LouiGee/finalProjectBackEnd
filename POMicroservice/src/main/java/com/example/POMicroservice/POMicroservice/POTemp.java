package com.example.POMicroservice.POMicroservice;

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


    private String ponumber;

    @Id
    private String poitemnumber;

    private String UserId;

    private String Company;
    private String Item;
    private String Unit;
    private int Quantity;
    private double Price;

    @CreatedDate
    @Column(nullable = true, updatable = false)
    private LocalDateTime dateRaised;

    public POTemp(String UserId, String Company, String Item, String Unit, int Quantity, double Price) {

        this.UserId = UserId;
        this.Company = Company;
        this.Item = Item;
        this.Unit = Unit;
        this.Quantity = Quantity;
        this.Price = Price;

    }

    public POTemp() {}


    public String getPonumber() {
        return ponumber;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public void setPoitemnumber(String poitemnumber) {
        this.poitemnumber = poitemnumber;
    }

    public void setPonumber(String ponumber) {
        this.ponumber = ponumber;
    }

    public String getPoitemnumber() {
        return poitemnumber;
    }

    public String getUserId() {
        return UserId;
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

