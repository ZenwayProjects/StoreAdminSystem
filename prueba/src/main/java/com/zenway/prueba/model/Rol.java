package com.zenway.prueba.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "rol")

public class Rol {

    @Id
    @Column(name = "rol_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long rol_id;

    @Column(name = "rol_nombre")
    private String rol_nombre;

}
