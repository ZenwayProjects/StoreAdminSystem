package com.zenway.prueba.service;

import com.zenway.prueba.model.Rol;
import com.zenway.prueba.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RolService {
    private final RolRepository rolRepository;

    @Autowired
    public RolService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    public List<Rol> listarRoles() {
        return rolRepository.findAll();
    }

    public Optional<Rol> obtenerRolPorId(Long id) {
        return rolRepository.findById(id);
    }

    public Rol crearRol(Rol rol) {
        return rolRepository.save(rol);
    }

    public Optional<Rol> actualizarRol(Long id, Rol rol) {
        Optional<Rol> rolExistente = rolRepository.findById(id);
        if (rolExistente.isPresent()) {
            Rol rolActual = rolExistente.get();
            rolActual.setRolNombre(rol.getRolNombre()); // Actualiza otros campos según sea necesario
            return Optional.of(rolRepository.save(rolActual));
        } else {
            return Optional.empty();
        }
    }

    public void eliminarRol(Long id) {
        rolRepository.deleteById(id);
    }
}
