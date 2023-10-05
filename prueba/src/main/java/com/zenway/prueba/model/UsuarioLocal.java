package com.zenway.prueba.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Objects;
import java.util.Random;

@Entity
@Data
@Table(name = "usuario_local")
public class UsuarioLocal {

    @Id
    @Column(name = "usuario_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long usuario_id;

    @ManyToOne
    @JoinColumn(name = "usl_usuario_id", referencedColumnName = "usuario_id")
    private Usuario uslUsuario;

    @ManyToOne
    @JoinColumn(name = "usl_local_id", referencedColumnName = "local_id")
    private LocalComercial uslLocal;



}
