package com.zenway.prueba.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "categoria")
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "categoria_id")
    private Long categoria_id;

    @Column(name = "cat_nombre")
    private String cat_nombre;

    @OneToMany(mappedBy = "local_categoria")
    private List<LocalComercial> locales;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "cat_padre")
    private List<SubCategoria> subcategorias;


}
