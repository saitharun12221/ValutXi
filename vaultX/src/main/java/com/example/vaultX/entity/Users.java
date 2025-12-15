package com.example.vaultX.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
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
    @OneToOne(mappedBy="user",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserDetails userDetails;
    @OneToOne(mappedBy="user",cascade = CascadeType.ALL,fetch=FetchType.LAZY)
    private Wallet wallet;
    @OneToMany(mappedBy="user",cascade=CascadeType.ALL,fetch=FetchType.LAZY)
    private List<Transactions> transaction;
    private boolean status;
    public Users(){
        this.createdAt=LocalDateTime.now();
        this.updatedAt=LocalDateTime.now();
    }
    public Users(String userName, String email, String password){
        this();
        this.userName=userName;
        this.email=email;
        this.password=password;
    }
    public Long getuserId(){
        return userId;
    }
    public void setuserId(Long userId){
        this.userId=userId;
    }
    public String getusername(){
        return userName;
    }
    public void setusername(String userName){
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
    public List<Transactions> getTransactions(){
        return transaction;
    }
    public void setTransactions(List<Transactions> transaction){
        this.transaction = transaction;
    }
    public boolean getstatus(){
        return status;
    }
    public void setstatus(boolean status){
        this.status= status;
    }


    
}
