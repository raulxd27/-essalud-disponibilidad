package com.essalud.api.security;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            CustomUserDetailsService userDetailsService) {

        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // Obtener cabecera de autorización
        final String authorizationHeader =
                request.getHeader("Authorization");

        // Verificar si existe el token Bearer
        if (authorizationHeader == null ||
                !authorizationHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }

        // Extraer el token JWT
        final String token = authorizationHeader.substring(7);

        try {

            // Extraer nombre de usuario del JWT
            String nombreUsuario =
                    jwtService.extractUsername(token);

            if (nombreUsuario != null &&
                    SecurityContextHolder.getContext()
                            .getAuthentication() == null) {

                // Buscar usuario en la base de datos
                UserDetails userDetails =
                        userDetailsService.loadUserByUsername(
                                nombreUsuario
                        );

                // Validar token y usuario
                if (jwtService.isTokenValid(token, userDetails)) {

                    // Crear objeto de autenticación
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    // Registrar detalles de la solicitud
                    authentication.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );

                    // Establecer autenticación en Spring Security
                    SecurityContextHolder.getContext()
                            .setAuthentication(authentication);

                    System.out.println(
                            "JWT VALIDADO CORRECTAMENTE PARA: "
                                    + nombreUsuario
                    );

                } else {

                    System.err.println(
                            "ERROR JWT: el token no coincide con el usuario "
                                    + "o ha expirado."
                    );
                }
            }

        } catch (JwtException | IllegalArgumentException |
                 UsernameNotFoundException exception) {

            // Diagnóstico del error de autenticación
            System.err.println(
                    "ERROR JWT: "
                            + exception.getClass().getSimpleName()
            );

            System.err.println(
                    "DETALLE: "
                            + exception.getMessage()
            );

            // Limpiar contexto de seguridad
            SecurityContextHolder.clearContext();
        }

        // Continuar con la cadena de filtros
        filterChain.doFilter(request, response);
    }
}