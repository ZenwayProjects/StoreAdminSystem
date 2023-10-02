package com.zenway.prueba.controller;

import com.zenway.prueba.model.LocalComercial;
import com.zenway.prueba.service.LocalComercialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/local/")
public class LocalComercialController {

    private final LocalComercialService localComercialService;

    @Autowired
    public LocalComercialController(LocalComercialService localComercialService) {
        this.localComercialService = localComercialService;
    }

    // Endpoint para crear un nuevo local comercial
    @PostMapping("crear")
    public ResponseEntity<LocalComercial> crearLocalComercial(@RequestBody LocalComercial nuevoLocal) {
        LocalComercial localCreado = localComercialService.crearLocalComercial(nuevoLocal);
        return new ResponseEntity<>(localCreado, HttpStatus.CREATED);
    }


}
