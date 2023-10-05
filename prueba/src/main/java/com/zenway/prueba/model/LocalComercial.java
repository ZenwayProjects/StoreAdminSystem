package com.zenway.prueba.model;

import jakarta.persistence.*;
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

    @Column(name = "local_representante")
    private String localRepresentanteLegal;


    @Column(name = "local_Estado")
    @Enumerated(EnumType.STRING)
    private local_estado localEstado;

    public static  enum local_estado{

        ACTIVO, INACTIVO, EN_DEUDA, EN_DESALOJO
    }


    @ManyToOne
    @JoinColumn(name = "local_subcategoria_id", referencedColumnName = "subcategoria_id")
    private SubCategoria localSubcategoria;

    @OneToMany(mappedBy = "uslLocal")
    private Set<UsuarioLocal> usuariosLocales = new HashSet<>();

    @OneToMany(mappedBy = "aud_local_id")
    private Set<Auditoria> audits = new HashSet<>();




}
