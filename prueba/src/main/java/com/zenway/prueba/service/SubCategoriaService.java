package com.zenway.prueba.service;

import com.zenway.prueba.model.Categoria;
import com.zenway.prueba.model.SubCategoria;
import com.zenway.prueba.repository.CategoriaRepository;
import com.zenway.prueba.repository.SubCategoriaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class SubCategoriaService {

    private final SubCategoriaRepository subCategoriaRepository;
    private final CategoriaRepository CategoriaRepository;

    @Autowired
    public SubCategoriaService(
            SubCategoriaRepository subCategoriaRepository,
            CategoriaRepository CategoriaRepository) {
        this.subCategoriaRepository = subCategoriaRepository;
        this.CategoriaRepository = CategoriaRepository;
    }



    // Método para listar subcategorías por el campo categoria_id de la relación cat_padre
    public List<SubCategoria> listarSubcategoriasPorCategoria(Long categoriaId) {
        return subCategoriaRepository.findByCatPadre_CategoriaId(categoriaId);
    }

    public List<SubCategoria> listarTodasSubCategorias() {
        return subCategoriaRepository.findAll();
    }

    // Método para crear una nueva subcategoría
    public SubCategoria crearSubCategoria(SubCategoria subCategoria, Long categoriaId) {
        // Busca la categoría padre por su Id
        Categoria categoriaPadre = CategoriaRepository.findById(categoriaId).orElse(null);

        if (categoriaPadre != null) {
            // Establece el nombre de la categoría padre en la subcategoría
            subCategoria.setCatPadreNombre(categoriaPadre.getCatNombre());
            // Establece la relación con la categoría padre
            subCategoria.setCatPadre(categoriaPadre);

            // Guarda la subcategoría en la base de datos
            return subCategoriaRepository.save(subCategoria);
        } else {
            // Maneja el caso en que la categoría padre no existe
            throw new IllegalArgumentException("La categoría padre no existe");
        }
    }

}
