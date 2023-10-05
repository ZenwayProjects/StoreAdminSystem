package com.zenway.prueba.repository;

import com.zenway.prueba.model.LocalComercial;
import com.zenway.prueba.model.Usuario;
import com.zenway.prueba.model.UsuarioLocal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface UsuarioLocalRepository extends JpaRepository<UsuarioLocal, Long>, JpaSpecificationExecutor<UsuarioLocal> {

    List<UsuarioLocal> findByUslUsuario(Usuario usuario);

    Set<UsuarioLocal> findByUslLocal(LocalComercial localComercial);
}
