package com.zenway.prueba.service;

import com.zenway.prueba.model.Categoria;
import com.zenway.prueba.model.SubCategoria;
import com.zenway.prueba.repository.CategoriaRepository;
import com.zenway.prueba.repository.SubCategoriaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final SubCategoriaRepository subCategoriaRepository;

    private final SubCategoriaService subCategoriaService;

    @Autowired
    public CategoriaService(CategoriaRepository categoriaRepository, SubCategoriaRepository subCategoriaRepository, SubCategoriaService subCategoriaService) {
        this.categoriaRepository = categoriaRepository;
        this.subCategoriaRepository = subCategoriaRepository;
        this.subCategoriaService = subCategoriaService;
    }

    // Método para obtener todas las categorías
    public List<Categoria> listarLasCategorias() {
        try{
            return categoriaRepository.findAll();
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<Categoria> listarCategoriasConSubcategorias() {
        return categoriaRepository.findAllWithSubcategorias();
    }

    // Método para crear una nueva categoría
    public Categoria crearCategoria(Categoria nuevaCategoria) {
        // Puedes realizar validaciones adicionales aquí si es necesario antes de guardar en la base de datos
        return categoriaRepository.save(nuevaCategoria);
    }

    public Optional<Categoria> buscarCategoriaDesdeSubcategoria(Long subcategoriaId) {
        System.out.println("subCategoria Id: " +subcategoriaId);

        SubCategoria subCategoriaOptional = subCategoriaService.obtenerSubcategoriaPorId(subcategoriaId);
        if (subCategoriaOptional !=null) {
            return Optional.ofNullable(subCategoriaOptional.getCatPadre());
        }
        return Optional.empty();
    }
}
