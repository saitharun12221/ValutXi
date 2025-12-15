package com.example.vaultX.dto;

import com.example.vaultX.entity.UserDetails;
import com.example.vaultX.entity.Users;

public class UserDto {
    private Long userId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    
    // Only include necessary fields, not relationships
    
    // Constructors, getters, setters
    public static UserDto fromEntity(Users user) {
        UserDto dto = new UserDto();
        dto.setuserId(user.getuserId());
        dto.setusername(user.getusername());
        dto.setemail(user.getemail());
        return dto;
    }
    public Long getuserId(){
        return userId;
    }
    public void setuserId(Long userId){
        this.userId=userId;
    }
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
}

