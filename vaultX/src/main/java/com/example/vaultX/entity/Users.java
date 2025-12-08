package com.example.vaultX.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="Users")
public class Users {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long userId;
    private String userName;
    @Column(name = "email", nullable = false  , unique = true )//firstName+lastName+nubers+symbols
    private String email;
    private String password;
    private LocalDateTime createdAt;// time and date when the account was created
    private LocalDateTime updatedAt;// only renders when password changed;
    // @OneToOne(cascade = CascadeType.ALL)
    // @JoinColumn(name = "user_details_fk")
    private UserDetails userDetails;
    // @OneToOne(cascade = CascadeType.ALL)
    // @JoinColumn(name = "wallet_fk")
    private Wallet wallet;
    private Transactions transaction;
    private boolean status;
    public Users(){

    }
    public Users(Long userId, String userName, String email, String password, LocalDateTime createdAt, LocalDateTime updatedAt,UserDetails userDetails, Wallet wallet,Transactions transaction, boolean status){
        this.userId=userId;
        this.userName=userName;
        this.email=email;
        this.password=password;
        this.createdAt=createdAt;
        this.updatedAt=updatedAt;
        this.userDetails=userDetails;
        this.wallet=wallet;
        this.transaction=transaction;
        this.status = status;
    }
    public Long getuserId(){
        return userId;
    }
    public void setuserId(Long userId){
        this.userId=userId;
    }
    public String getuserName(){
        return userName;
    }
    public void setuserName(String userName){
        this.userName=userName;
    }
    public String getemail(){
        return email;
    }
    public void setemail(String email){
        this.email=email;
    }
    public String getpassword(){
        return password;
    }
    public void setpassword(String password){
        this.password=password;
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
    public UserDetails getuserDetails(){
        return userDetails;
    }
    public void setuserDetails(UserDetails userDetails){
        this.userDetails=userDetails;
    }
    public Wallet getwallet(){
        return wallet;
    }
    public void setWallet(Wallet wallet){
        this.wallet=wallet;
    }
    public Transactions gettTransactions(){
        return transaction;
    }
    public void setTransactions(Transactions transaction){
        this.transaction = transaction;
    }
    public boolean getstatus(){
        return status;
    }
    public void setstatus(boolean status){
        this.status= status;
    }


    
}
