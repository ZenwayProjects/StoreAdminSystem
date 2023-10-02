package com.zenway.prueba.controller;

import com.zenway.prueba.model.SubCategoria;
import com.zenway.prueba.repository.SubCategoriaRepository;
import com.zenway.prueba.service.SubCategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subcategoria/")
public class SubCategoriaController {
    private final SubCategoriaService subCategoriaService;

    @Autowired
    public SubCategoriaController(SubCategoriaService subCategoriaService) {
        this.subCategoriaService = subCategoriaService;
    }

    @GetMapping("listar")
    public ResponseEntity<List<SubCategoria>> listarTodasSubCategorias() {
        List<SubCategoria> subCategorias = subCategoriaService.listarTodasSubCategorias();
        return new ResponseEntity<>(subCategorias, HttpStatus.OK);
    }

    // Endpoint para obtener subcategorías por el campo categoria_id de la relación catPadre
    @GetMapping("listar/cat_padre/{categoriaId}")
    public ResponseEntity<List<SubCategoria>> obtenerSubCategoriasPorCategoria(@PathVariable Long categoriaId) {
        List<SubCategoria> subCategorias = subCategoriaService.listarSubcategoriasPorCategoria(categoriaId);
        return new ResponseEntity<>(subCategorias, HttpStatus.OK);
    }



    // Endpoint para crear una nueva subcategoría
    @PostMapping("/crear/{categoriaId}")
    public ResponseEntity<SubCategoria> crearSubcategoria(@RequestBody SubCategoria subCategoria, @PathVariable Long categoriaId) {
        SubCategoria nuevaSubcategoria = subCategoriaService.crearSubCategoria(subCategoria, categoriaId);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaSubcategoria);
    }
}
