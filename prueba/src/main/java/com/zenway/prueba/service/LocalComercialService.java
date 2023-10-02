package com.zenway.prueba.service;

import com.zenway.prueba.model.LocalComercial;
import com.zenway.prueba.repository.LocalComercialRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class LocalComercialService {

    private final LocalComercialRepository localComercialRepository;

    @Autowired
    public LocalComercialService(LocalComercialRepository localComercialRepository) {
        this.localComercialRepository = localComercialRepository;
    }

    // Método para crear un nuevo local comercial
    public LocalComercial crearLocalComercial(LocalComercial nuevoLocal) {
        // Puedes realizar validaciones adicionales aquí si es necesario antes de guardar en la base de datos
        return localComercialRepository.save(nuevoLocal);
    }



}
