package com.zenway.prueba.repository;

import com.zenway.prueba.model.LocalComercial;
import com.zenway.prueba.model.UsuarioLocal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface LocalComercialRepository extends JpaRepository<LocalComercial, Long>, JpaSpecificationExecutor<LocalComercial> {
}
