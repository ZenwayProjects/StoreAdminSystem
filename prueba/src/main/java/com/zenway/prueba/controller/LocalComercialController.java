package com.zenway.prueba.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/local/")
public class LocalComercialController {

    @PostMapping
    ("crear")
    public String crearLocalComercial() {
        return "Crear local comercial";
    }


}
