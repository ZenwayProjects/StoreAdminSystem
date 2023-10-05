package com.zenway.prueba.security;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration//Le indica al contenedor de Spring que esta clase es de seguridad
@EnableWebSecurity
//Indicamos que se activa la seguridad web en la app y ademas es una clase que contendra toda la configuracion referente a la seguridad
public class SecurityConfig {
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Value("classpath:rutas.json")
    private Resource rutaConfigResource;

    @Autowired
    public SecurityConfig(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint){
        this.jwtAuthenticationEntryPoint =jwtAuthenticationEntryPoint;
    }
    //Este bean va a encargarse de verificar la info de los usuarios que se loguearan en nuestra api
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }
    //Este bean encripta todas las contraseñas
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    //Este bean incorpora el filtro de seguridad de JWT
    @Bean
    JwtAuthenticationFilter jwtAuthenticationFilter(){
        return new JwtAuthenticationFilter();
    }


    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
//        List<RutaConfig> rutaConfigs = loadRutasFromJson();
//        // Agregar un mensaje de depuración para verificar las rutas cargadas desde el JSON
//        System.out.println("Rutas cargadas desde el JSON: " + rutaConfigs.toString());
        http.csrf((csrf) -> csrf.disable())
            //permitimos el manejo de excepciones
            .exceptionHandling((exceptionHandling) ->
                    exceptionHandling
                            .authenticationEntryPoint(jwtAuthenticationEntryPoint))
            //Establece el punto de entrada personalizado de autenticacion para el manejo de las autenticaciones no autorizadas
            .sessionManagement((sessionManagement) ->
                    sessionManagement
                            .sessionCreationPolicy(SessionCreationPolicy.STATELESS))//Permite la gestion de sesiones
            .cors(cors -> cors
            .configurationSource(request -> {
                CorsConfiguration corsConfig = new CorsConfiguration();
                corsConfig.addAllowedOrigin("http://localhost:4200"); // Permitir el origen de tu aplicación frontend
                corsConfig.addAllowedOrigin("/**");
                corsConfig.addAllowedHeader("*");
                corsConfig.addExposedHeader("Access-Control-Allow-Origin");
                corsConfig.addExposedHeader("Access-Control-Allow-Credentials");
                corsConfig.addAllowedMethod("*");
                corsConfig.setAllowCredentials(true);
                corsConfig.setMaxAge(3600L);
                return corsConfig;
            }))


            //Toda peticion debe ser autorizada
            .authorizeHttpRequests(authorizeHttpRequests -> {
                authorizeHttpRequests
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/roles/**").permitAll()
                        .requestMatchers("/hola").permitAll();
                /*for (RutaConfig rutaConfig : rutaConfigs) {
                    for (String role : rutaConfig.getRoles()) {
                        System.out.println(role + "  "+ rutaConfig.getRuta());
                        authorizeHttpRequests
                                .requestMatchers(HttpMethod.valueOf(rutaConfig.getMetodo()), rutaConfig.getRuta())
                                .hasAnyAuthority(role);
                    }
                }*/
                authorizeHttpRequests
                    .requestMatchers(HttpMethod.GET, "/api/usuario/list", "/api/usuario/mi-info").hasAnyAuthority("ADMIN", "VIGILANTE", "USUARIO_LOCAL")
                    .requestMatchers(HttpMethod.GET, "/api/usuario/{id}").hasAuthority("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/api/usuario/delete/{id}").hasAuthority("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/api/usuario/update/{id}").hasAnyAuthority("ADMIN", "VIGILANTE")
                    .requestMatchers(HttpMethod.GET, "/api/local/listar").hasAnyAuthority("ADMIN", "VIGILANTE", "USUARIO_LOCAL")
                    .requestMatchers(HttpMethod.POST, "/api/categoria/crear").hasAuthority("ADMIN")
                    .requestMatchers(HttpMethod.GET, "/api/categoria/listar").hasAnyAuthority("ADMIN", "USUARIO_LOCAL")
                    .requestMatchers(HttpMethod.GET, "/api/subcategoria/crear", "/api/subcategoria/listar").hasAuthority("ADMIN")
                    .requestMatchers(HttpMethod.POST, "/api/local/crear").hasAuthority("ADMIN")
                    .requestMatchers(HttpMethod.GET, "/api/local/listar").hasAuthority("ADMIN")
                    .requestMatchers(HttpMethod.GET, "/api/local/eliminar/{id}").hasAuthority("ADMIN")
                    .requestMatchers(HttpMethod.GET, "/api/local/actualizar/{id}").hasAuthority("ADMIN")
                    .requestMatchers(HttpMethod.POST, "/api/asignacion/usuario").hasAuthority("ADMIN");



                authorizeHttpRequests.anyRequest().authenticated();
            }).httpBasic(withDefaults());
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


    private List<RutaConfig> loadRutasFromJson() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(rutaConfigResource.getInputStream(), new TypeReference<>() {
        });
    }
}
