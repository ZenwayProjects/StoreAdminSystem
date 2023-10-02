package com.zenway.prueba.model;

import jakarta.persistence.*;
import jdk.jfr.DataAmount;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "local_comercial")
public class LocalComercial {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "local_id")
    private Long localId;

    @Column(name = "local_nombre")
    private String localNombre;

    @Column(name = "local_ubicacion")
    private String localUbicacion;

    @Column(name = "local_celular")
    private String localCelular;


    @Enumerated(EnumType.STRING)
    private local_estado localEstado;

    public static  enum local_estado{

        ACTIVO, INACTIVO, EN_DEUDA, EN_DESALOJO
    }




    @ManyToOne
    @JoinColumn(name = "local_categoria_id", referencedColumnName = "categoria_id")
    private Categoria local_categoria;

    @ManyToOne
    @JoinColumn(name = "local_subcategoria_id", referencedColumnName = "subcategoria_id")
    private SubCategoria local_subcategoria;

    @OneToMany(mappedBy = "usl_local")
    private Set<UsuarioLocal> usuariosLocales = new HashSet<>();

    @OneToMany(mappedBy = "aud_local_id")
    private Set<Auditoria> audits = new HashSet<>();




}
