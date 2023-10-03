package com.zenway.prueba.controller;

import com.zenway.prueba.dto.LocalComercialDTO;
import com.zenway.prueba.model.LocalComercial;
import com.zenway.prueba.service.LocalComercialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/local/")
public class LocalComercialController {

    private final LocalComercialService localComercialService;

    @Autowired
    public LocalComercialController(LocalComercialService localComercialService) {
        this.localComercialService = localComercialService;
    }

    @GetMapping("listar")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @ResponseBody
    public ResponseEntity<List<LocalComercial>> listarLocales() {
        List<LocalComercial> locales = localComercialService.listarLocales();
        return ResponseEntity.ok(locales);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocalComercial> obtenerLocalPorId(@PathVariable Long id) {
        LocalComercial local = localComercialService.obtenerLocalPorId(id);
        return ResponseEntity.ok(local);
    }
    // Endpoint para crear un nuevo local comercial
    @PostMapping("crear")
    public ResponseEntity<LocalComercialDTO> crearLocalComercial(@RequestBody LocalComercialDTO localComercialDTO) {
        var localCreado = localComercialService.crearLocalComercial(localComercialDTO);
        return new ResponseEntity<>(localCreado, HttpStatus.CREATED);
    }

    @PutMapping("actualizar/{id}")
    public ResponseEntity<LocalComercial> actualizarLocalComercial(
            @PathVariable Long id,
            @RequestBody LocalComercial localComercial) {
        LocalComercial localActualizado = localComercialService.actualizarLocalComercial(id, localComercial);
        return ResponseEntity.ok(localActualizado);
    }


}
