package com.zenway.prueba.model;

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
    private Long subcat_id;

    @Column(name = "subcat_nombre")
    private String subc_nombre;

    @ManyToOne
    @JoinColumn(name = "cat_padre_id", referencedColumnName = "categoria_id")
    private Categoria cat_padre;

    @OneToMany(mappedBy = "local_subcategoria")
    private List<LocalComercial> locales;




}
