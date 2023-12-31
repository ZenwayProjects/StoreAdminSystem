package com.zenway.prueba.repository;

import com.zenway.prueba.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long>, JpaSpecificationExecutor<Categoria> {
    Optional<Categoria> findByCatNombre(String nombre);

    @Query("SELECT c FROM Categoria c LEFT JOIN FETCH c.subcategorias")
    List<Categoria> findAllWithSubcategorias();


}
