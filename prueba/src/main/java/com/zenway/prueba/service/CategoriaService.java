package com.zenway.prueba.service;

import com.zenway.prueba.model.Categoria;
import com.zenway.prueba.repository.CategoriaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    @Autowired
    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    // Método para obtener todas las categorías
    public List<Categoria> listarLasCategorias() {
        return categoriaRepository.findAll();
    }

    // Método para crear una nueva categoría
    public Categoria crearCategoria(Categoria nuevaCategoria) {
        // Puedes realizar validaciones adicionales aquí si es necesario antes de guardar en la base de datos
        return categoriaRepository.save(nuevaCategoria);
    }
}
