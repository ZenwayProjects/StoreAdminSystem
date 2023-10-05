package com.zenway.prueba.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class LocalComercialDTO {
    private String localNombre;
    private String localUbicacion;
    private String localCelular;
    private String localEstado;
    private String localRepresentanteLegal;
    private Long localSubcategoriaId;
}
