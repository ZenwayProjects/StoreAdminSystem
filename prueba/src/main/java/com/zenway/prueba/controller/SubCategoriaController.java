package com.zenway.prueba.controller;

import com.zenway.prueba.model.SubCategoria;
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
    @GetMapping("/listarsubcategoria/{categoriaId}")
    public ResponseEntity<List<SubCategoria>> listarSubcategoriasPorCategoria(@PathVariable Long categoriaId) {
        List<SubCategoria> subcategorias = subCategoriaService.listarSubcategoriasPorCategoria(categoriaId);
        return ResponseEntity.ok(subcategorias);
    }



    // Endpoint para crear una nueva subcategoría
    @PostMapping("crear")
    public ResponseEntity<SubCategoria> crearSubcategoria(@RequestBody SubCategoria subCategoria) {
        SubCategoria nuevaSubcategoria = subCategoriaService.crearSubCategoria(subCategoria);
        return new ResponseEntity<>(nuevaSubcategoria, HttpStatus.CREATED);
    }

    @GetMapping("{subcategoriaId}")
    public ResponseEntity<SubCategoria> obtenerSubcategoriaPorId(@PathVariable Long subcategoriaId) {
        SubCategoria subcategoria = subCategoriaService.obtenerSubcategoriaPorId(subcategoriaId);
        if (subcategoria != null) {
            return ResponseEntity.ok(subcategoria);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
