package com.zenway.prueba.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity
@Data
@Table(name = "usuario_rol")
public class UsuarioRol {

    @Id
    @Column(name = "usuario_rol_id")
    private Long usuario_rol_id;

    @Column(name = "usr_usuario_id")
    private Long usuario_id;


    @Column(name = "usr_rol_id")
    private Long rol_id;

    // Otros atributos y métodos
}



