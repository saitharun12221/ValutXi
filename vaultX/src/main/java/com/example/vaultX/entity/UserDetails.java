package com.example.vaultX.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "userDetails")
public class UserDetails {
    private String firstName;
    private String lastName;
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @OneToOne
    @JoinColumn(name = "userId")
    private Long userId;//const crypto = require('crypto');

// function generateHashId(email, timestamp) {
//   const hash = crypto
//     .createHash('sha256')
//     .update(email + timestamp + Math.random().toString())
//     .digest('hex')
//     .substring(0, 16); // Take first 16 chars
//   return hash;
// }
    private String phone;
    private String address;
    private Users user;
    public UserDetails(){

    }
    public UserDetails(String firstName,String lastName, Long userId, String phone, String address, Users user){
        this.firstName=firstName;
        this.lastName=lastName;
        this.userId=userId;
        this.phone = phone;
        this.address= address;
        this.user = user;
    }
    public String getfirstName(){
        return firstName;
    }
    public void setfirstName(String firstName){this.firstName=firstName;}
    public String getlastName(){
        return lastName;
    }
    public void setlastName(String lastName){this.lastName=lastName;}
    public Long getuserId(){
        return userId;
    }
    public void setuserId(Long userId){this.userId=userId;
    }
     public String getphone(){
        return phone;
    }
    public void setphone(String phone){this.phone=phone;}
    public String getaddress(){
        return address;
    }
    public void setaddress(String address){this.address=address;}
    public Users getuser(){
        return user;
    }
    public void setusers(Users user){
        this.user=user;
    }



}
