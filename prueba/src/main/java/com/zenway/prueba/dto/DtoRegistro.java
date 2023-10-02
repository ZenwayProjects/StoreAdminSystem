package com.zenway.prueba.dto;

import jakarta.persistence.Column;
import lombok.Data;

import java.util.Date;

@Data
public class DtoRegistro {
    private String nombre;

    private String apellido;

    private Date fechaNacimiento;

    private String email;

    private String direccion;

    private int telefono;
}
