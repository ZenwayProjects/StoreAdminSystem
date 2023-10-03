package com.zenway.prueba.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "subcategoria")
public class SubCategoria {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "subcategoria_id")
    private Long id;

    @Column(name = "subcat_nombre")
    private String subcNombre;

    @ManyToOne
    @JoinColumn(name = "cat_padre_id", referencedColumnName = "categoria_id")
    private Categoria catPadre;

    @JsonIgnore
    @OneToMany(mappedBy = "localSubcategoria")
    private List<LocalComercial> locales;






}
