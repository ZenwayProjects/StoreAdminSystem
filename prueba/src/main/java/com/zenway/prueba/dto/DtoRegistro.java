package com.zenway.prueba.dto;

import jakarta.persistence.Column;
import lombok.Data;

import java.util.Date;

@Data
public class DtoRegistro {
    private String nombre;

    private String apellidos;

    private String login;

    private String password;

    private String numeroDocumento;
}
