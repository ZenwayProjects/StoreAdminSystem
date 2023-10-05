package com.zenway.prueba.controller;

import com.zenway.prueba.dto.DtoAuthRespuesta;
import com.zenway.prueba.dto.DtoLogin;
import com.zenway.prueba.dto.DtoRegistro;
import com.zenway.prueba.model.Rol;
import com.zenway.prueba.model.Usuario;
import com.zenway.prueba.repository.RolRepository;
import com.zenway.prueba.repository.UsuarioRepository;
import com.zenway.prueba.security.JwtGenerador;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth/")
public class RestControllerAuth {
    private AuthenticationManager authenticationManager;

    private PasswordEncoder passwordEncoder;
    private RolRepository rolRepo;
    private UsuarioRepository usuarioRepo;
    private JwtGenerador jwtGenerador;

    @Autowired

    public RestControllerAuth(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, RolRepository rolRepo, UsuarioRepository usuarioRepo, JwtGenerador jwtGenerador) {
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.rolRepo = rolRepo;
        this.usuarioRepo = usuarioRepo;
        this.jwtGenerador = jwtGenerador;
    }



    //Controlador encargado de hacer las peticiones que registran a los usuarios
    @PostMapping("registrar-admin")
    public ResponseEntity<String> registrar(@RequestBody DtoRegistro dtoRegistro) {
        if (usuarioRepo.existsByLogin(dtoRegistro.getLogin())) {
            return new ResponseEntity<>("El usuario ya existe, intenta con otro", HttpStatus.BAD_REQUEST);
        }



        // Crear una instancia de Usuario con la información del DTO
        Usuario usuario = new Usuario();
        usuario.setNombre(dtoRegistro.getNombre());
        usuario.setApellidos(dtoRegistro.getApellidos());
        usuario.setLogin(dtoRegistro.getLogin());
        usuario.setPassword(passwordEncoder.encode(dtoRegistro.getPassword()));
        usuario.setNumeroDocumento(dtoRegistro.getNumeroDocumento());

        Rol rol = rolRepo.findByRolNombre("ADMIN").orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        usuario.setRoles(Collections.singletonList(rol));
        usuarioRepo.save(usuario);

        return new ResponseEntity<>("Registro de usuario exitoso", HttpStatus.OK);
    }

    //Método para poder logear un usuario y obtener un token
    @PostMapping("login")
    public ResponseEntity<DtoAuthRespuesta> login(@RequestBody DtoLogin dtoLogin) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dtoLogin.getLogin(), dtoLogin.getPassword()));

        System.out.println(authentication.getAuthorities().toString());

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerador.generarToken(authentication);
        DtoAuthRespuesta authRespuesta = new DtoAuthRespuesta(token);
        authRespuesta.setRoles(authentication.getAuthorities().toString());
        return new ResponseEntity<>(authRespuesta, HttpStatus.OK);
    }

}
