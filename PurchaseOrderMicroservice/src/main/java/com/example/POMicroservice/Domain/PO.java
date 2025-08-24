

package com.example.POMicroservice.Domain;

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


    private String ponumber;

    @Id
    private String poitemnumber;

    private String Company;
    private String Item;
    private String Unit;
    private int Quantity;
    private double Price;
    private String status;

    @CreatedDate
    @Column(nullable = true, updatable = false)
    private LocalDateTime dateRaised;
    private String raisedBy;
    private String approvedBy;
    private LocalDateTime dateApproved;
    private String Description;
    private String paidBy;
    private LocalDateTime datePaid;

    public PO( String PONumber, String POItemNumber, String Company, String Item, String Unit, int Quantity , double Price, LocalDateTime dateRaised, String raisedBy, String Status, String description ) {

        this.ponumber = PONumber;
        this.poitemnumber = POItemNumber;
        this.Company = Company;
        this.Item = Item;
        this.Unit = Unit;
        this.Quantity = Quantity;
        this.Price = Price;
        this.dateRaised = dateRaised;
        this.raisedBy = raisedBy;
        this.status = Status;
        this.Description = description;

    }

    public PO( String PONumber, String POItemNumber, String Company, String Item, String Unit, int Quantity , double Price, LocalDateTime dateRaised, String raisedBy, LocalDateTime dateApproved, String approvedBy, String Status, String description ) {

        this.ponumber = PONumber;
        this.poitemnumber = POItemNumber;
        this.Company = Company;
        this.Item = Item;
        this.Unit = Unit;
        this.Quantity = Quantity;
        this.Price = Price;
        this.dateRaised = dateRaised;
        this.raisedBy = raisedBy;
        this.dateApproved = dateApproved;
        this.approvedBy = approvedBy;
        this.status = Status;
        this.Description = description;

    }

    public String getPonumber() {
        return ponumber;
    }

    public String getPoitemnumber() {
        return poitemnumber;
    }

    public PO() {}

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

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public void setDateApproved(LocalDateTime dateApproved) {
        this.dateApproved = dateApproved;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setRaisedBy(String raisedBy) {
        this.raisedBy = raisedBy;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setPaidBy(String paidBy) {
        this.paidBy = paidBy;
    }

    public void setPaidDate(LocalDateTime paidDate) {
        this.datePaid = paidDate;
    }
}


