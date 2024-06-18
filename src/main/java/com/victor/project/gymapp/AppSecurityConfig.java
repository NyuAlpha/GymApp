package com.victor.project.gymapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.victor.project.gymapp.services.JpaUserDetailsService;

/*
 * Clase de configuración de seguridad con Spring Security
 */
@Configuration
public class AppSecurityConfig {
   


   
    //Se inyecta los beans personalizados necesarios para esta clase

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    @Autowired
    private JpaUserDetailsService userDetailService;

    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;







    /*
     * configuramos el AuthenticationManagerBuilder para que tome la clase UserDetailService personalizada 
     * y le añade el enconder que utilizará.
     */
    @Autowired
    public void userDetailsService(AuthenticationManagerBuilder build) throws Exception {
        build.userDetailsService(userDetailService)
        .passwordEncoder(passwordEncoder);
    }




    /*
     * Añade toda la capa de seguridad, restricción de endpoints para distintos roles y usuarios
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception { 
        return http
                //Rutas protegidas y permitidas por diferentes roles
                .authorizeHttpRequests(
                    (authz) -> authz
                        .requestMatchers("/css/**", "/js/**", "/img/**", "/signin")
                        .permitAll()
                        .requestMatchers("/admin/control")
                        .hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                //Establece las características del login
                .formLogin(login -> login
                    .loginPage("/login")
                    .failureUrl("/login?error=true")//aunque viene por defecto, se pueden enviar parámetros
                    .successHandler(customAuthenticationSuccessHandler)
                    .permitAll())
                //Todos tienenn permiso para logout
                .logout(logout -> logout.permitAll())
                //Cuando no hay permiso para una acción envia al handler /error que lanzará en este caso un error 403
                .exceptionHandling(ex -> ex.accessDeniedPage("/error"))
                .build();
    }
        
}
