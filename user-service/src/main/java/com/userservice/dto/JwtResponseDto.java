package com.userservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class JwtResponseDto {

    private String token;
    private String type = "Bearer";
    private String id;
    private String username;
    private String email;
    private List<String> roles;

    public JwtResponseDto(String token, String type, String id, String username,String email, List<String> roles) {
        this.token = token;
        this.type = type;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }
}
