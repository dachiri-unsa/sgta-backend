package com.sgta.security.filter;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.sgta.security.jwt.JwtService;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService,
                                    UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
                                    throws ServletException, IOException {

        // 1. Obtener header
        String authHeader = request.getHeader("Authorization");

        // 2. Validar formato
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extraer token
        String token = authHeader.substring(7);

        // 4. Extraer username del token
        String username = jwtService.extractUsername(token);
		
		// Paso extra, controlar si falla token
		try {
        // 5. Verificar si no está autenticado ya
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
	
	            // 6. Cargar usuario desde BD
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
	
	            // 7. Validar token
                if (jwtService.validateToken(token, userDetails)) {
	
	                // 8. Crear autenticación
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );
	
	                // 9. Registrar en Spring Security
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (JwtException | UsernameNotFoundException e) {
		    // Token inválido → simplemente no autenticar, el filtro continúa
		    // Spring devolverá 401 por sí solo
		}

        // 10. Continuar la cadena
        filterChain.doFilter(request, response);
    }
}
