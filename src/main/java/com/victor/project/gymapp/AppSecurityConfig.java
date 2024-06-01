package com.victor.project.gymapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.victor.project.gymapp.services.JpaUserDetailsService;

@Configuration
public class AppSecurityConfig {
   
   
   @Autowired
   private BCryptPasswordEncoder passwordEncoder;
 
   @Autowired
   private JpaUserDetailsService userDetailService;

   @Autowired
   private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

   @Autowired
   public void userDetailsService(AuthenticationManagerBuilder build) throws Exception {
      build.userDetailsService(userDetailService)
      .passwordEncoder(passwordEncoder);
   }
 
   @Bean
   public SecurityFilterChain filterChain(HttpSecurity http) throws Exception { 
    return http
            .authorizeHttpRequests(
                (authz) -> authz
                    .requestMatchers("/css/**", "/js/**", "/img/**", "/signin")
                    .permitAll()
                    .requestMatchers("/admin/control")
                    .hasRole("ADMIN")
                    .anyRequest().authenticated()
            )
            .formLogin(login -> login
                .loginPage("/login")
                .failureUrl("/login?error=true")//aunque viene por defecto, se pueden enviar parámetros
                .successHandler(customAuthenticationSuccessHandler)
                .permitAll())
            .logout(logout -> logout.permitAll())
            .exceptionHandling(ex -> ex.accessDeniedPage("/error_403"))
            .build();
    }
}
