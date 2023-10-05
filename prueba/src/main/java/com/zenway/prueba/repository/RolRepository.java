package com.zenway.prueba.repository;

import com.zenway.prueba.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long>,  JpaSpecificationExecutor<Rol>
{
    Optional<Rol> findByRolNombre(String rol_nombre);
}
