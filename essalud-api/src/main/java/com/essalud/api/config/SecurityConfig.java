package com.essalud.api.config;

import com.essalud.api.security.CustomUserDetailsService;
import com.essalud.api.security.JwtAuthenticationFilter;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            CustomUserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {

        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    // ==========================================
    // AUTENTICACION DE USUARIOS
    // ==========================================

    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(userDetailsService);

        provider.setPasswordEncoder(passwordEncoder);

        return provider;
    }

    // ==========================================
    // CONFIGURACION CORS PARA ANGULAR
    // ==========================================

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        // Origen autorizado del frontend Angular
        configuration.setAllowedOrigins(
                List.of("http://localhost:4200")
        );

        // Metodos HTTP permitidos
        configuration.setAllowedMethods(
                List.of("GET", "POST", "PUT", "PATCH", "OPTIONS")
        );

        // Encabezados permitidos
        configuration.setAllowedHeaders(
                List.of("Authorization", "Content-Type", "Accept")
        );

        // Encabezados visibles para Angular
        configuration.setExposedHeaders(
                List.of("Authorization")
        );

        // No utilizamos cookies de sesion
        configuration.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    // ==========================================
    // CONFIGURACION DE SEGURIDAD
    // ==========================================

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http

            // Desactivar CSRF para API REST
            .csrf(csrf -> csrf.disable())

            // Habilitar CORS
            .cors(cors ->
                    cors.configurationSource(corsConfigurationSource())
            )

            // API sin sesiones de servidor
            .sessionManagement(session ->
                    session.sessionCreationPolicy(
                            SessionCreationPolicy.STATELESS
                    )
            )

            // Proveedor de autenticacion
            .authenticationProvider(authenticationProvider())

            // ==========================================
            // MANEJO DE ERRORES HTTP 401 Y 403
            // ==========================================

            .exceptionHandling(exception -> exception

                // Usuario no autenticado
                .authenticationEntryPoint(
                    (request, response, authException) -> {

                        response.setStatus(
                                HttpServletResponse.SC_UNAUTHORIZED
                        );

                        response.setContentType(
                                MediaType.APPLICATION_JSON_VALUE
                        );

                        response.setCharacterEncoding(
                                StandardCharsets.UTF_8.name()
                        );

                        response.getWriter().write(
                                "{\"estado\":401,"
                                + "\"error\":\"No autenticado\","
                                + "\"mensaje\":\"Debe iniciar sesión o "
                                + "presentar un token válido.\"}"
                        );
                    }
                )

                // Usuario autenticado sin permisos
                .accessDeniedHandler(
                    (request, response, accessDeniedException) -> {

                        response.setStatus(
                                HttpServletResponse.SC_FORBIDDEN
                        );

                        response.setContentType(
                                MediaType.APPLICATION_JSON_VALUE
                        );

                        response.setCharacterEncoding(
                                StandardCharsets.UTF_8.name()
                        );

                        response.getWriter().write(
                                "{\"estado\":403,"
                                + "\"error\":\"Acceso denegado\","
                                + "\"mensaje\":\"No tiene permisos para "
                                + "realizar esta operación.\"}"
                        );
                    }
                )
            )

            // ==========================================
            // AUTORIZACION POR ROLES
            // ==========================================

            .authorizeHttpRequests(auth -> auth

                // Solicitudes preliminares CORS
                .requestMatchers(
                    HttpMethod.OPTIONS,
                    "/**"
                ).permitAll()

                // CONSULTAS PUBLICAS
                .requestMatchers(
                    HttpMethod.GET,
                    "/api/establecimientos",
                    "/api/especialidades",
                    "/api/medicamentos",
                    "/api/citas",
                    "/api/disponibilidad/medicamentos"
                ).permitAll()

                // AUTENTICACION PUBLICA
                .requestMatchers(
                    "/api/auth/**"
                ).permitAll()

                // ==========================================
                // GESTION DE MEDICAMENTOS
                // ==========================================

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/medicamentos"
                ).hasAnyRole(
                    "FARMACIA",
                    "ADMINISTRADOR_SISTEMA"
                )

                .requestMatchers(
                    HttpMethod.PUT,
                    "/api/medicamentos/**"
                ).hasAnyRole(
                    "FARMACIA",
                    "ADMINISTRADOR_SISTEMA"
                )

                // ==========================================
                // ACTUALIZACION DE INVENTARIO
                // ==========================================

                .requestMatchers(
                    "/api/inventario/**",
                    "/api/disponibilidad/medicamentos/**"
                ).hasAnyRole(
                    "FARMACIA",
                    "ADMINISTRADOR_SISTEMA"
                )

                // ==========================================
                // GESTION DE CITAS
                // ==========================================

                // Publicar disponibilidad de citas
                .requestMatchers(
                    HttpMethod.POST,
                    "/api/citas"
                ).hasAnyRole(
                    "PROGRAMADOR_CITAS",
                    "ADMINISTRADOR_SISTEMA"
                )

                // Modificar disponibilidad de citas
                .requestMatchers(
                    HttpMethod.PUT,
                    "/api/citas/**"
                ).hasAnyRole(
                    "PROGRAMADOR_CITAS",
                    "ADMINISTRADOR_SISTEMA"
                )

                // Cambiar estado de las citas
                .requestMatchers(
                    HttpMethod.PATCH,
                    "/api/citas/**"
                ).hasAnyRole(
                    "PROGRAMADOR_CITAS",
                    "ADMINISTRADOR_SISTEMA"
                )

                // Impedir eliminación física de citas
                .requestMatchers(
                    HttpMethod.DELETE,
                    "/api/citas/**"
                ).denyAll()

                // ==========================================
                // ADMINISTRACION DEL SISTEMA
                // ==========================================

                .requestMatchers(
                    "/api/usuarios/**",
                    "/api/roles/**"
                ).hasRole(
                    "ADMINISTRADOR_SISTEMA"
                )

                // CUALQUIER OTRA OPERACION REQUIERE AUTENTICACION
                .anyRequest().authenticated()
            )

            // Desactivar formularios de inicio de sesion
            .formLogin(form -> form.disable())

            // Desactivar autenticacion HTTP Basic
            .httpBasic(basic -> basic.disable())

            // Incorporar filtro JWT
            .addFilterBefore(
                    jwtAuthenticationFilter,
                    UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}