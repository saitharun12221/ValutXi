package com.example.vaultX.dto;

import java.time.LocalDate;

public class UsersUpdateDto {
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
    private LocalDate dateofbirth;
    public String getusername(){
        return username;
    }
    public void setusername(String username){
        this.username=username;
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
    public String getfirstName(){
        return firstName;
    }
    public void setfirstName(String firstName){
        this.firstName=firstName;
    }
    public String getlastName(){
        return lastName;
    }
    public void setlastName(String lastName){
        this.lastName=lastName;
    }
    public String getphone(){
        return phone;
    }
    public void setphone(String phone){
        this.phone=phone;
    }
    public LocalDate getdateofbirth(){
        return dateofbirth;
    }
    public void setdateofbirth(LocalDate dateofbirth){
        this.dateofbirth=dateofbirth;
    }
}
