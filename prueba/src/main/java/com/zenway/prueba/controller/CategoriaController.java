package com.zenway.prueba.controller;

import com.zenway.prueba.model.Categoria;
import com.zenway.prueba.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categoria/")
public class CategoriaController {
    private final CategoriaService categoriaService;

    @Autowired
    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    // Endpoint para obtener todas las categorías
    @GetMapping("listar")
    public ResponseEntity<List<Categoria>> listarTodasLasCategorias() {
        List<Categoria> categorias = categoriaService.listarLasCategorias();
        return new ResponseEntity<>(categorias, HttpStatus.OK);
    }

    @GetMapping("/listar/consubcategorias")
    public List<Categoria> listarCategoriasConSubcategorias() {
        return categoriaService.listarCategoriasConSubcategorias();
    }

    // Endpoint para crear una nueva categoría
    @PostMapping("crear")
    public ResponseEntity<Categoria> crearCategoria(@RequestBody Categoria nuevaCategoria) {
        Categoria categoriaCreada = categoriaService.crearCategoria(nuevaCategoria);
        return new ResponseEntity<>(categoriaCreada, HttpStatus.CREATED);
    }


}
