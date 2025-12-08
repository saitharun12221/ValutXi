package com.example.vaultX.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.example.vaultX.enums.CurrencyType;
import com.example.vaultX.enums.TransactionalStatus;
import com.example.vaultX.enums.TransactionalType;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name="Transactions")
public class Transactions {
    private Long userId;
    private String fromWallet;
    private String toWallet;
    private BigDecimal amount;
    private CurrencyType currency;
    private TransactionalType type;
    private String description;
    private TransactionalStatus status;
    private LocalDate createdAt;
    private Users user;
    public Transactions(){

    }

      public Transactions(BigDecimal amount, TransactionalType type, Users user) {
        this();
        this.amount = amount;
        this.type = type;
        this.user = user;
    }
    public Long getuserId(){
        return userId;
    }
    public void setuserId(Long userId){
        this.userId=userId;
    }
    public String getfromWallet(){
        return fromWallet;
    }
    public void setfromWallet(String fromWallet){
        this.fromWallet=fromWallet;
    }
    public String gettoWallet(){
        return toWallet;
    }
    public void settoWallet(String toWallet){
        this.toWallet=toWallet;
    }
    public BigDecimal getamount(){
        return amount;
    }
    public void setamount(BigDecimal amount){
        this.amount=amount;
    }
    public CurrencyType getcurrency(){
        return currency;
    }
    public void setcurrency(CurrencyType currency){
        this.currency=currency;
    }
    public TransactionalType gettype(){
        return type;
    }
    public void settype(TransactionalType type){
        this.type=type;
    }
    public String getdescription(){
        return description;
    }
    public void setdescription(String description){
        this.description=description;
    }
    public TransactionalStatus getstatus(){
        return status;
    }
    public void setstatus(TransactionalStatus status){
        this.status=status;
    }
    public LocalDate getcreatedAt(){
        return createdAt;
    }
    public void setcreatedAt(LocalDate createdAt){
        this.createdAt=createdAt;
    }

}
