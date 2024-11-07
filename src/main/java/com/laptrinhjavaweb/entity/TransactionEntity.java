package com.laptrinhjavaweb.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "transaction")
public class TransactionEntity extends BaseEntity {
    @Column(name = "note")
    private String note;
    @Column(name = "code")
    private String code;
    @Column(name = "staffid")
    private Long staffId;
    @ManyToOne
    @JoinColumn(name ="customerid")
    private CustomerEntity customer;
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public CustomerEntity getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
    }
}
