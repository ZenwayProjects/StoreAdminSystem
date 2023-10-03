package com.zenway.prueba.security;


import com.zenway.prueba.model.Rol;
import com.zenway.prueba.model.Usuario;
import com.zenway.prueba.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetails implements UserDetailsService {
    private UsuarioRepository usuarioRepo;

    @Autowired
    public CustomUserDetails(UsuarioRepository usuarioRepo) {
        this.usuarioRepo = usuarioRepo;
    }
    //metodo para juntar todas las autoridades en una lista
    public Collection<GrantedAuthority> mapToAuthorities(List<Rol> roles){
        return roles.stream().map(rol -> new SimpleGrantedAuthority(rol.getRolNombre())).collect(Collectors.toList());
    }

    //Metodo para traernos un usuario con todos sus datos por medio de sus username
    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepo.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        return new User(usuario.getLogin(), usuario.getPassword(), mapToAuthorities(usuario.getRoles()));
    }
}
