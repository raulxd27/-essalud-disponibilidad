package com.essalud.api.service;

import com.essalud.api.entity.HistorialActualizacion;
import com.essalud.api.entity.Usuario;
import com.essalud.api.repository.HistorialActualizacionRepository;
import com.essalud.api.repository.UsuarioRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HistorialActualizacionService {

    private final HistorialActualizacionRepository historialRepository;
    private final UsuarioRepository usuarioRepository;

    public HistorialActualizacionService(
            HistorialActualizacionRepository historialRepository,
            UsuarioRepository usuarioRepository) {

        this.historialRepository = historialRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // Listar todos los registros del historial
    @Transactional(readOnly = true)
    public List<HistorialActualizacion> listarTodos() {

        return historialRepository.findAll();
    }

    // Buscar un registro del historial por su ID
    @Transactional(readOnly = true)
    public HistorialActualizacion buscarPorId(Long id) {

        return historialRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Registro de historial no encontrado: " + id
                        )
                );
    }

    // Obtener el usuario responsable de una operación
    @Transactional(readOnly = true)
    public Usuario obtenerUsuario(String nombreUsuario) {

        return usuarioRepository
                .findByNombreUsuario(nombreUsuario)
                .orElseThrow(() ->
                        new RuntimeException(
                                "No se encontró el usuario responsable: "
                                + nombreUsuario
                        )
                );
    }

    // Registrar una operación administrativa
    @Transactional
    public HistorialActualizacion registrar(
            String nombreUsuario,
            String tipoRegistro,
            Integer registroId,
            String accion,
            String descripcion,
            String valorAnterior,
            String valorNuevo) {

        Usuario usuario = obtenerUsuario(nombreUsuario);

        HistorialActualizacion historial =
                new HistorialActualizacion();

        historial.setUsuario(usuario);
        historial.setTipoRegistro(tipoRegistro);
        historial.setRegistroId(registroId);
        historial.setAccion(accion);
        historial.setDescripcion(descripcion);
        historial.setValorAnterior(valorAnterior);
        historial.setValorNuevo(valorNuevo);
        historial.setFechaActualizacion(LocalDateTime.now());

        return historialRepository.save(historial);
    }
}