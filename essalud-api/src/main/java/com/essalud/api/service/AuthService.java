package com.essalud.api.service;

import com.essalud.api.dto.LoginRequestDTO;
import com.essalud.api.dto.LoginResponseDTO;
import com.essalud.api.entity.Usuario;
import com.essalud.api.repository.UsuarioRepository;
import com.essalud.api.security.JwtService;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;

    public AuthService(
            AuthenticationManager authenticationManager,
            UsuarioRepository usuarioRepository,
            JwtService jwtService) {

        this.authenticationManager = authenticationManager;
        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
    }

    @Transactional(readOnly = true)
    public LoginResponseDTO login(LoginRequestDTO request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getNombreUsuario(),
                        request.getContrasena()
                )
        );

        Usuario usuario = usuarioRepository
                .findByNombreUsuario(request.getNombreUsuario())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Credenciales incorrectas"
                        )
                );

        String token = jwtService.generateToken(
                org.springframework.security.core.userdetails.User
                        .withUsername(usuario.getNombreUsuario())
                        .password(usuario.getContrasenaHash())
                        .authorities(
                                "ROLE_" + usuario.getRol().getNombreRol()
                        )
                        .build()
        );

        return new LoginResponseDTO(
                token,
                usuario.getUsuarioId(),
                usuario.getNombreUsuario(),
                usuario.getNombres(),
                usuario.getApellidos(),
                usuario.getCorreo(),
                usuario.getRol().getNombreRol()
        );
    }
}