package com.example.vaultX.dto;

import com.example.vaultX.entity.Users;
public class UserDto{
    private Long userId;
    private String username;
    private String email;
    
    // Only include necessary fields, not relationships
    
    // Constructors, getters, setters

    // Getters and Setters
    
    
    public static UserDto fromEntity(Users user) {
        UserDto dto = new UserDto();
        dto.setuserId(user.getuserId());
        dto.setusername(user.getusername());
        dto.setemail(user.getemail());
        
        // Check what methods are available in your Users entity
        // Option A: If Users has getFirstName()/getLastName()
    
        // Option B: If Users extends/extracts from UserDetails
        // Check what fields are actually in your Users class
        
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
}

