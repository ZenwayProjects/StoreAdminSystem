package com.zenway.prueba.dto;

import com.zenway.prueba.model.Rol;
import lombok.Data;

import java.util.List;

@Data
public class DtoAuthRespuesta {
    private String accessToken;
    private String tokenType = "Bearer ";
    private String roles;
    public DtoAuthRespuesta(String accessToken){
        this.accessToken = accessToken;
    }
}
