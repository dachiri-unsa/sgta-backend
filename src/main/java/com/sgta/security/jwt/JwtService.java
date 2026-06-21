package com.sgta.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtService {

    private final JwtConfig jwtConfig;
    private final SecretKey key;

    public JwtService(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
        // Clave precomputada (mejor rendimiento)
        this.key = Keys.hmacShaKeyFor(
                jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8)
        );
    }
    
    // GENERAR NUEVO
    public String generateToken(UserDetails userDetails) {
        Instant now = Instant.now();

		List<String> roles = userDetails.getAuthorities()  
				.stream()  
				.map(auth -> auth.getAuthority())  
				.filter(auth -> auth.startsWith("ROLE_"))  
				.map(auth -> auth.replace("ROLE_", ""))  
				.toList();  

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("roles", roles)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusMillis(jwtConfig.getExpiration())))
                .signWith(key)
                .compact();
    }
    
    // Extraer username
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    // Validar token
    public boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    // Verificar expiración
    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    // Parser moderno (privado)
    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
