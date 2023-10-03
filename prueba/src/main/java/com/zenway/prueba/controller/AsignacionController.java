package com.zenway.prueba.controller;

import com.zenway.prueba.dto.DtoAsignacion;
import com.zenway.prueba.service.AsignacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/asignacion")
public class AsignacionController {
    private final AsignacionService asignacionService;

    @Autowired
    public AsignacionController(AsignacionService asignacionService) {
        this.asignacionService = asignacionService;
    }

    @PostMapping("/usuario")
    public ResponseEntity<String> asignarUsuarioLocal(@RequestBody DtoAsignacion dtoAsignacion) {
        try {
            asignacionService.asignarRol(dtoAsignacion);
            return ResponseEntity.ok("Usuario asignado como USUARIO_LOCAL exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al asignar el usuario como USUARIO_LOCAL.");
        }
    }
}
