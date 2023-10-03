package com.zenway.prueba.repository;

import com.zenway.prueba.model.SubCategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubCategoriaRepository extends JpaRepository<SubCategoria, Long>, JpaSpecificationExecutor<SubCategoria> {
    // Método para buscar subcategorías por el ID de la categoría
    List<SubCategoria> findByCatPadre_CategoriaId(Long categoriaId);
}
