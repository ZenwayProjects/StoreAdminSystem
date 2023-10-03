package com.zenway.prueba.repository;

import com.zenway.prueba.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>, JpaSpecificationExecutor<Usuario> {
    //metodo para buscarlo por su login o username o lo que sea
    Optional<Usuario> findByLogin(String login);

    //Metodo para verificar si un usuario existe en nuestra BD
    Boolean existsByLogin(String login);
}
