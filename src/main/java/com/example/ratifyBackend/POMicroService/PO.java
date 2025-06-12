

package com.example.ratifyBackend.POMicroService;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Entity
@Table(name = "purchaseOrder")
public class PO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // or UUID if preferred
    private Long id;

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

    public PO(String PONumber, String POItemNumber, String Company, String Item, String Unit, int Quantity , double Price, LocalDateTime dateRaised ) {
        this.ponumber = PONumber;
        this.poitemnumber = POItemNumber;
        this.Company = Company;
        this.Item = Item;
        this.Unit = Unit;
        this.Quantity = Quantity;
        this.Price = Price;
        this.dateRaised = dateRaised;

    }

    public PO() {

    }

    public void setId(Long id) {
        this.id = id;
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

    public String getPOItemNumber() {
        return poitemnumber;
    }

    public String getPONumber() {
        return ponumber;
    }

    public Long getId() {
        return id;
    }
}


