package com.sgta.security.config;

import java.util.List;

import org.springframework.http.HttpMethod;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.sgta.security.filter.JwtAuthenticationFilter;

import lombok.AllArgsConstructor;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        var configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            .authorizeHttpRequests(auth -> auth
                // Auth
                .requestMatchers("/auth/**").permitAll()

                // Usuarios
                .requestMatchers(HttpMethod.GET, "/api/users/minimal/mechanics")
                    .hasAnyRole("ADMIN", "RECEPTIONIST", "MECHANIC")
                .requestMatchers("/api/users/**").hasRole("ADMIN")

                // Clientes
                .requestMatchers(HttpMethod.GET, "/api/customers/**")
                    .hasAnyRole("ADMIN", "MECHANIC", "RECEPTIONIST", "CASHIER", "INVENTORY_MANAGER")
                .requestMatchers(HttpMethod.POST, "/api/customers/**")
                    .hasAnyRole("ADMIN", "RECEPTIONIST")
                .requestMatchers(HttpMethod.PUT, "/api/customers/**")
                    .hasAnyRole("ADMIN", "RECEPTIONIST")
                .requestMatchers(HttpMethod.DELETE, "/api/customers/**")
                    .hasRole("ADMIN")

                // Vehículos
                .requestMatchers(HttpMethod.GET, "/api/vehicles/**")
                    .hasAnyRole("ADMIN", "MECHANIC", "RECEPTIONIST", "INVENTORY_MANAGER")
                .requestMatchers(HttpMethod.POST, "/api/vehicles/**")
                    .hasAnyRole("ADMIN", "RECEPTIONIST")
                .requestMatchers(HttpMethod.PUT, "/api/vehicles/**")
                    .hasAnyRole("ADMIN", "RECEPTIONIST")
                .requestMatchers(HttpMethod.DELETE, "/api/vehicles/**")
                    .hasRole("ADMIN")

                // Repuestos
                .requestMatchers(HttpMethod.GET, "/api/parts/**")
                    .hasAnyRole("ADMIN", "MECHANIC", "RECEPTIONIST", "INVENTORY_MANAGER")
                .requestMatchers(HttpMethod.POST, "/api/parts/**")
                    .hasAnyRole("ADMIN", "INVENTORY_MANAGER")
                .requestMatchers(HttpMethod.PUT, "/api/parts/**")
                    .hasAnyRole("ADMIN", "INVENTORY_MANAGER")
                .requestMatchers(HttpMethod.DELETE, "/api/parts/**")
                    .hasAnyRole("ADMIN", "INVENTORY_MANAGER")

                // Ordenes de Trabajo
                .requestMatchers(HttpMethod.GET, "/api/work-orders/**")
                    .hasAnyRole("ADMIN", "MECHANIC", "RECEPTIONIST", "CASHIER", "INVENTORY_MANAGER")
                .requestMatchers(HttpMethod.POST, "/api/work-orders/**")
                    .hasAnyRole("ADMIN", "RECEPTIONIST")
                .requestMatchers(HttpMethod.PUT, "/api/work-orders/**")
                    .hasAnyRole("ADMIN", "MECHANIC")
                .requestMatchers(HttpMethod.DELETE, "/api/work-orders/**")
                    .hasRole("ADMIN")

                // Partes de OT
                .requestMatchers(HttpMethod.GET, "/api/work-order-parts/**")
                    .hasAnyRole("ADMIN", "MECHANIC", "RECEPTIONIST", "CASHIER", "INVENTORY_MANAGER")
                .requestMatchers(HttpMethod.POST, "/api/work-order-parts/**")
                    .hasAnyRole("ADMIN", "MECHANIC")
                .requestMatchers(HttpMethod.PUT, "/api/work-order-parts/**")
                    .hasAnyRole("ADMIN", "MECHANIC")
                .requestMatchers(HttpMethod.DELETE, "/api/work-order-parts/**")
                    .hasRole("ADMIN")

                // Facturas
                .requestMatchers(HttpMethod.GET, "/api/invoices/**")
                    .hasAnyRole("ADMIN", "RECEPTIONIST", "CASHIER")
                .requestMatchers(HttpMethod.POST, "/api/invoices/**")
                    .hasAnyRole("ADMIN", "CASHIER")
                .requestMatchers(HttpMethod.PUT, "/api/invoices/**")
                    .hasAnyRole("ADMIN", "CASHIER")
                .requestMatchers(HttpMethod.DELETE, "/api/invoices/**")
                    .hasAnyRole("ADMIN", "CASHIER")

                .anyRequest().authenticated()
            )

            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
