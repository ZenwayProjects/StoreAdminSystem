package com.zenway.prueba.service;

import com.zenway.prueba.dto.LocalComercialDTO;
import com.zenway.prueba.model.LocalComercial;
import com.zenway.prueba.model.SubCategoria;
import com.zenway.prueba.repository.LocalComercialRepository;
import com.zenway.prueba.repository.SubCategoriaRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LocalComercialService {

    private final LocalComercialRepository localComercialRepository;
    private final SubCategoriaRepository subCategoriaRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public LocalComercialService(LocalComercialRepository localComercialRepository, SubCategoriaRepository subCategoriaRepository, ModelMapper modelMapper) {
        this.localComercialRepository = localComercialRepository;
        this.subCategoriaRepository = subCategoriaRepository;
        this.modelMapper = modelMapper;
    }

    public List<LocalComercial> listarLocales() {
        return localComercialRepository.findAll();
    }

    public LocalComercial obtenerLocalPorId(Long localId) {
        Optional<LocalComercial> localOptional = localComercialRepository.findById(localId);
        if (localOptional.isPresent()) {
            return localOptional.get();
        } else {
            throw new IllegalArgumentException("El local comercial con ID " + localId + " no existe.");
        }
    }

    // Método para crear un nuevo local comercial
    public LocalComercialDTO crearLocalComercial(LocalComercialDTO localComercial) {
        // Guarda la SubCategoria si existe en el LocalComercial
        var subCategoria = new SubCategoria();
        subCategoria.setId(localComercial.getLocalSubcategoriaId());
        var toEntity = modelMapper.map(localComercial, LocalComercial.class);
        toEntity.setLocalSubcategoria(subCategoria);
        // Guarda el LocalComercial en la base de datos
        return modelMapper.map(localComercialRepository.save(toEntity), LocalComercialDTO.class);
    }

    // Método para actualizar un local comercial existente
    public LocalComercial actualizarLocalComercial(Long id, LocalComercial localComercialActualizado) {
        // Verificar si el local con el ID dado existe
        LocalComercial localExistente = localComercialRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Local no encontrado con ID: " + id));

        // Actualizar los campos relevantes del local existente con los valores del nuevo local
        localExistente.setLocalNombre(localComercialActualizado.getLocalNombre());
        localExistente.setLocalUbicacion(localComercialActualizado.getLocalUbicacion());
        localExistente.setLocalCelular(localComercialActualizado.getLocalCelular());
        localExistente.setLocalEstado(localComercialActualizado.getLocalEstado());
        localExistente.setLocalSubcategoria(localComercialActualizado.getLocalSubcategoria());

        // Guardar el local comercial actualizado en la base de datos
        return localComercialRepository.save(localExistente);
    }



}
