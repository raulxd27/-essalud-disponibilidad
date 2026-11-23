package com.essalud.api.service;

import com.essalud.api.entity.Usuario;
import com.essalud.api.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // Listar usuarios activos
    public List<Usuario> listarActivos() {
        return usuarioRepository.findAll()
                .stream()
                .filter(Usuario::getActivo)
                .toList();
    }

    // Buscar usuario activo por nombre de usuario
    public Usuario buscarActivoPorNombreUsuario(String nombreUsuario) {
        return usuarioRepository.findByNombreUsuario(nombreUsuario)
                .filter(Usuario::getActivo)
                .orElseThrow(() ->
                        new RuntimeException("Usuario no encontrado o inactivo"));
    }

    // Buscar usuario por ID
    public Usuario buscarPorId(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Usuario no encontrado: " + id));
    }

    // Verificar si existe un nombre de usuario
    public boolean existeNombreUsuario(String nombreUsuario) {
        return usuarioRepository.existsByNombreUsuario(nombreUsuario);
    }

    // Verificar si existe un correo electrónico
    public boolean existeCorreo(String correo) {
        return usuarioRepository.existsByCorreo(correo);
    }
}
