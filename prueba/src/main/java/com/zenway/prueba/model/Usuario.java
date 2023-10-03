package com.zenway.prueba.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Table(name = "usuario")
public class Usuario {
    @Id
    @Column(name = "usuario_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long usuario_id;

    @Column(name = "usu_login")
    private String login;

    @Column(name = "usu_password")
    private String password;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellidos")
    private String apellidos;

    @Column(name = "numero_documento")
    private String numeroDocumento;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "usuario_rol",
            joinColumns = @JoinColumn(name = "usr_usuario_id", referencedColumnName = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "usr_rol_id", referencedColumnName = "rol_id"))
    private List<Rol> roles = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "usuario_local",
            joinColumns = @JoinColumn(name = "usl_usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "usl_local_id")
    )
    private Set<LocalComercial> localesComerciales = new HashSet<>();

    @OneToMany(mappedBy = "usl_usuario")
    private Set<UsuarioLocal> usuariosLocales = new HashSet<>();

    @OneToMany(mappedBy = "aud_usuario_id")
    private Set<Auditoria> audits = new HashSet<>();



}
