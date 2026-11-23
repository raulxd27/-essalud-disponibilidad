package com.essalud.api.security;

import com.essalud.api.entity.Usuario;
import com.essalud.api.repository.UsuarioRepository;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String nombreUsuario)
            throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository
                .findByNombreUsuario(nombreUsuario)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuario no encontrado"
                ));

        if (!Boolean.TRUE.equals(usuario.getActivo())) {
            throw new UsernameNotFoundException(
                    "La cuenta del usuario está inactiva"
            );
        }

        if (usuario.getRol() == null ||
                !Boolean.TRUE.equals(usuario.getRol().getActivo())) {
            throw new UsernameNotFoundException(
                    "El rol del usuario está inactivo"
            );
        }

        String autoridad = "ROLE_" +
                usuario.getRol().getNombreRol().trim().toUpperCase();

        return User.builder()
                .username(usuario.getNombreUsuario())
                .password(usuario.getContrasenaHash())
                .authorities(List.of(
                        new SimpleGrantedAuthority(autoridad)
                ))
                .build();
    }
}