package com.zenway.prueba.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "categoria")
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "categoria_id")
    private Long categoriaId;

    @Column(name = "cat_nombre", unique = true, nullable = false)
    private String catNombre;

//    @JsonIgnore
    @OneToMany(mappedBy = "catPadre", fetch = FetchType.LAZY)
    private List<SubCategoria> subcategorias= new ArrayList<>();


}
