package com.daniil.library.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class AuthResponseDTO {

    private String accessToken;
    private String tokenType = "Bearer ";
    private List<String> roles;

    public AuthResponseDTO(String accessToken, List<String> roles) {
        this.accessToken = accessToken;
        this.roles = roles;
    }
}
