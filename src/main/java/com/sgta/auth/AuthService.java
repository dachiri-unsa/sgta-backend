package com.sgta.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.sgta.auth.dto.LoginRequest;
import com.sgta.security.jwt.JwtService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

	public String login(LoginRequest request) {

        // 1. VALIDAR CREDENCIALES 
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
            )
        );

        // 2. OBTENER USUARIO AUTENTICADO
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        
		return jwtService.generateToken(userDetails);
    }
}
