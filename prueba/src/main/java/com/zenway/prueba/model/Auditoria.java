package com.zenway.prueba.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "auditoria")
public class Auditoria {

    @Id
    @Column(name = "auditoria_id")
    private Long auditoria_id;

    @ManyToOne
    @JoinColumn(name = "aud_usuario_id", referencedColumnName = "usuario_id")
    private Usuario aud_usuario_id;

    @Column(name = "aud_fecha")
    private String aud_fecha;

    @ManyToOne
    @JoinColumn(name = "aud_local_id", referencedColumnName = "local_id")
    private LocalComercial aud_local_id;



}
