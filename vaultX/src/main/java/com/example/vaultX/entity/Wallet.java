package com.example.vaultX.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.vaultX.enums.CurrencyType;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="Wallet")
public class Wallet {
    private int wallet_id;
    private BigDecimal balance;
    private CurrencyType currency;//enum
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isActive;
    // @OneToOne(mappedBy = "wallet")
    // private Users user;
    private Users user;
    public Wallet(){

    }
    public Wallet(int wallet_id, BigDecimal balance, CurrencyType currency,LocalDateTime createdAt, LocalDateTime updatedAt, boolean isActive, Users user){
        this.wallet_id=wallet_id;
        this.balance=balance;
        this.currency=currency;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isActive = isActive;
        this.user = user;
    }
    public int getwallet_id(){
        return wallet_id;
    }
    public void setwallet_id(int wallet_id){
        this.wallet_id=wallet_id;
    }
    public BigDecimal getbalance(){
        return balance;
    }
    public void setbalance(BigDecimal balance){
        this.balance=balance;
    }
    public CurrencyType getCurrency(){
        return currency;
    }
    public void setCurrency(CurrencyType currency){
        this.currency=currency;
    }
    public LocalDateTime getcreatedAt(){
        return createdAt;
    }
    public void setcreatedAt(LocalDateTime createdAt){
        this.createdAt=createdAt;
    }
    public LocalDateTime getupdatedAt(){
        return updatedAt;
    }
    public void setupdatedAt(LocalDateTime updatedAt){
        this.updatedAt=updatedAt;
    }
    public boolean isActive(){
        return isActive;
    }
    public Users getuser(){
        return user;
    }
    public void setuser(Users user){
        this.user=user;
    }
}
