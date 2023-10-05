package com.zenway.prueba.service;

import com.zenway.prueba.dto.LocalComercialDTO;
import com.zenway.prueba.model.*;
import com.zenway.prueba.repository.*;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LocalComercialService {

    private final LocalComercialRepository localComercialRepository;
    private final SubCategoriaRepository subCategoriaRepository;
    private final UsuarioLocalRepository usuarioLocalRepository;
    private final UsuarioRepository usuarioRepository;
    private final CategoriaService categoriaService;
    private final ModelMapper modelMapper;

    @Autowired
    public LocalComercialService(LocalComercialRepository localComercialRepository, SubCategoriaRepository subCategoriaRepository, CategoriaRepository categoriaRepository, UsuarioRepository usuarioRepository, UsuarioLocalRepository usuarioLocalRepository, UsuarioRepository usuarioRepository1, CategoriaService categoriaService, ModelMapper modelMapper) {
        this.localComercialRepository = localComercialRepository;
        this.subCategoriaRepository = subCategoriaRepository;
        this.usuarioLocalRepository = usuarioLocalRepository;
        this.usuarioRepository = usuarioRepository1;
        this.categoriaService = categoriaService;
        this.modelMapper = modelMapper;
    }

    public List<LocalComercial> listarLocales() {
        List<LocalComercial> locales = localComercialRepository.findAll();
        locales.forEach(local -> {
            if (local.getLocalSubcategoria() != null){
                Categoria categoriaPadreOptional = categoriaService.buscarCategoriaDesdeSubcategoria(local.getLocalSubcategoria().getId()).orElse(null);
                if (categoriaPadreOptional != null){
                    System.out.println("Categoria: " +categoriaPadreOptional.getCatNombre());
                    local.getLocalSubcategoria().setCatPadre(categoriaPadreOptional);
                }
            }
        });
        return locales;
    }


    public LocalComercial obtenerLocalPorId(Long localId) {
        Optional<LocalComercial> localOptional = localComercialRepository.findById(localId);
        if (localOptional.isPresent()) {
            Categoria categoriaPadreOptional = categoriaService.buscarCategoriaDesdeSubcategoria(localOptional.get().getLocalSubcategoria().getId()).orElse(null);
            if (categoriaPadreOptional != null){
                System.out.println("Categoria: " +categoriaPadreOptional.getCatNombre());
                localOptional.get().getLocalSubcategoria().setCatPadre(categoriaPadreOptional);
            }

            return localOptional.get();
        } else {
            throw new IllegalArgumentException("El local comercial con ID " + localId + " no existe.");

        }
    }

    public List<LocalComercial> obtenerLocalPorUsuario() {
        System.out.println("principal: "+ SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Usuario usuario = usuarioRepository.findByLogin(user.getUsername()).orElseThrow();
        System.out.println("usuario: "+ usuario);
        List<UsuarioLocal> usuariolocal = usuarioLocalRepository.findByUslUsuario(usuario);
        System.out.println("usuario Local: "+ usuariolocal.get(0).toString());
        List<LocalComercial> locales = new ArrayList<>();
        usuariolocal.forEach(usuarioLocal -> {

            Optional<LocalComercial> localOptional = localComercialRepository.findById(usuarioLocal.getUslLocal().getLocalId());
            if (localOptional.isPresent()) {
                System.out.println("localID: "+ localOptional.get().getLocalId());
                Categoria categoriaPadreOptional = categoriaService.buscarCategoriaDesdeSubcategoria(localOptional.get().getLocalSubcategoria().getId()).orElse(null);
                if (categoriaPadreOptional != null){
                    System.out.println("Categoria: " +categoriaPadreOptional.getCatNombre());
                    localOptional.get().getLocalSubcategoria().setCatPadre(categoriaPadreOptional);
                }

                locales.add(localOptional.get());
            } else {
                throw new IllegalArgumentException("El local comercial con ID " + usuarioLocal.getUslLocal().getLocalId() + " no existe.");

            }
        });
        return locales;
    }

    // Método para crear un nuevo local comercial
    public LocalComercialDTO crearLocalComercial(LocalComercialDTO localComercial) {
        try{
            System.out.println(localComercial.toString());
            // Guarda la SubCategoria si existe en el LocalComercial
            SubCategoria subCategoria = subCategoriaRepository.findById(localComercial.getLocalSubcategoriaId())
                    .orElseThrow(() -> new IllegalArgumentException("SubCategoria no encontrada con ID: " + localComercial.getLocalSubcategoriaId()));

            var toEntity = modelMapper.map(localComercial, LocalComercial.class);
            toEntity.setLocalSubcategoria(subCategoria);
            // Guarda el LocalComercial en la base de datos
            return modelMapper.map(localComercialRepository.save(toEntity), LocalComercialDTO.class);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    // Método para actualizar un local comercial existente
    public LocalComercial actualizarLocalComercial(Long id, LocalComercial localComercialActualizado) {
        // Verificar si el local con el ID dado existe
        LocalComercial localExistente = localComercialRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Local no encontrado con ID: " + id));
        System.out.println(localComercialActualizado.toString());
        // Actualizar los campos relevantes del local existente con los valores del nuevo local
        localExistente.setLocalNombre(localComercialActualizado.getLocalNombre());
        localExistente.setLocalUbicacion(localComercialActualizado.getLocalUbicacion());
        localExistente.setLocalRepresentanteLegal(localComercialActualizado.getLocalRepresentanteLegal());
        localExistente.setLocalCelular(localComercialActualizado.getLocalCelular());
        localExistente.setLocalEstado(localComercialActualizado.getLocalEstado());
        localExistente.setLocalSubcategoria(localComercialActualizado.getLocalSubcategoria());

        // Guardar el local comercial actualizado en la base de datos
        return localComercialRepository.save(localExistente);
    }

    public LocalComercial eliminarLocalComercial(Long id) {
        LocalComercial localComercial = localComercialRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Local no encontrado con ID: " + id));
        localComercialRepository.deleteById(id);
        return localComercial;  // 204 No Content
    }



}
