package com.zenway.prueba.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "usuario_local")
public class UsuarioLocal {

    @Id
    @Column(name = "usuario_id")
    private Long usuario_id;

    @ManyToOne
    @JoinColumn(name = "usl_usuario_id", referencedColumnName = "usuario_id")
    private Usuario usl_usuario;

    @ManyToOne
    @JoinColumn(name = "usl_local_id", referencedColumnName = "local_id")
    private LocalComercial usl_local;

    @ManyToOne
    @JoinColumn(name = "usl_rol_id", referencedColumnName = "rol_id")
    private Rol usl_rol;




}
