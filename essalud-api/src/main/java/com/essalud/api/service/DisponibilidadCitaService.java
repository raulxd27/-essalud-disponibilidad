package com.essalud.api.service;

import com.essalud.api.dto.ActualizarDisponibilidadCitaDTO;
import com.essalud.api.dto.CambiarEstadoCitaDTO;
import com.essalud.api.dto.CrearDisponibilidadCitaDTO;
import com.essalud.api.entity.DisponibilidadCita;
import com.essalud.api.entity.Especialidad;
import com.essalud.api.entity.Establecimiento;
import com.essalud.api.repository.DisponibilidadCitaRepository;
import com.essalud.api.repository.EspecialidadRepository;
import com.essalud.api.repository.EstablecimientoRepository;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class DisponibilidadCitaService {

    private final DisponibilidadCitaRepository disponibilidadRepository;
    private final EspecialidadRepository especialidadRepository;
    private final EstablecimientoRepository establecimientoRepository;
    private final HistorialActualizacionService historialService;

    public DisponibilidadCitaService(
            DisponibilidadCitaRepository disponibilidadRepository,
            EspecialidadRepository especialidadRepository,
            EstablecimientoRepository establecimientoRepository,
            HistorialActualizacionService historialService) {

        this.disponibilidadRepository = disponibilidadRepository;
        this.especialidadRepository = especialidadRepository;
        this.establecimientoRepository = establecimientoRepository;
        this.historialService = historialService;
    }

    // =====================================================
    // CONSULTAS EXISTENTES
    // =====================================================

    @Transactional(readOnly = true)
    public List<DisponibilidadCita> listarPorEspecialidad(
            Integer especialidadId) {

        return disponibilidadRepository
                .findByEspecialidad_EspecialidadIdAndEstadoIgnoreCaseAndCantidadCuposGreaterThanAndFechaAtencionGreaterThanEqual(
                        especialidadId,
                        "Disponible",
                        0,
                        LocalDate.now()
                );
    }

    @Transactional(readOnly = true)
    public List<DisponibilidadCita> listarPorEstablecimientoYFecha(
            Integer establecimientoId,
            LocalDate fecha) {

        return disponibilidadRepository
                .findByEstablecimiento_EstablecimientoIdAndFechaAtencionAndEstadoIgnoreCaseAndCantidadCuposGreaterThanAndFechaAtencionGreaterThanEqual(
                        establecimientoId,
                        fecha,
                        "Disponible",
                        0,
                        LocalDate.now()
                );
    }

    @Transactional(readOnly = true)
    public List<DisponibilidadCita> buscarDisponibilidad(
            Integer especialidadId,
            Integer establecimientoId,
            LocalDate fecha) {

        return disponibilidadRepository
                .findByEspecialidad_EspecialidadIdAndEstablecimiento_EstablecimientoIdAndFechaAtencionAndEstadoIgnoreCaseAndCantidadCuposGreaterThanAndFechaAtencionGreaterThanEqual(
                        especialidadId,
                        establecimientoId,
                        fecha,
                        "Disponible",
                        0,
                        LocalDate.now()
                );
    }

    @Transactional(readOnly = true)
    public DisponibilidadCita buscarPorId(Integer id) {

        return disponibilidadRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Disponibilidad de cita no encontrada: " + id
                        )
                );
    }

    // =====================================================
    // PUBLICAR DISPONIBILIDAD DE CITAS
    // =====================================================

    @Transactional
    public DisponibilidadCita publicar(
            CrearDisponibilidadCitaDTO dto) {

        validarDatos(
                dto.especialidadId(),
                dto.establecimientoId(),
                dto.fechaAtencion(),
                dto.horaInicio(),
                dto.horaFin(),
                dto.cantidadCupos(),
                true
        );

        Especialidad especialidad = especialidadRepository
                .findById(dto.especialidadId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "La especialidad indicada no existe."
                        )
                );

        if (!Boolean.TRUE.equals(especialidad.getActivo())) {
            throw new IllegalArgumentException(
                    "No se puede publicar una cita para una especialidad inactiva."
            );
        }

        Establecimiento establecimiento = establecimientoRepository
                .findById(dto.establecimientoId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "El establecimiento indicado no existe."
                        )
                );

        if (!Boolean.TRUE.equals(establecimiento.getActivo())) {
            throw new IllegalArgumentException(
                    "No se puede publicar una cita para un establecimiento inactivo."
            );
        }

        validarDuplicado(
                dto.especialidadId(),
                dto.establecimientoId(),
                dto.fechaAtencion(),
                dto.horaInicio(),
                dto.horaFin()
        );

        DisponibilidadCita cita = new DisponibilidadCita();

        cita.setEspecialidad(especialidad);
        cita.setEstablecimiento(establecimiento);
        cita.setFechaAtencion(dto.fechaAtencion());
        cita.setHoraInicio(dto.horaInicio());
        cita.setHoraFin(dto.horaFin());
        cita.setCantidadCupos(dto.cantidadCupos());
        cita.setEstado("Disponible");
        cita.setFechaActualizacion(LocalDateTime.now());
        cita.setActualizadoPor(obtenerUsuarioAutenticado());

        DisponibilidadCita guardada =
                disponibilidadRepository.save(cita);

        historialService.registrar(
                obtenerNombreUsuario(),
                "DisponibilidadCita",
                guardada.getDisponibilidadCitaId(),
                "CREAR",
                "Publicación de disponibilidad de citas.",
                null,
                describir(guardada)
        );

        return buscarPorId(guardada.getDisponibilidadCitaId());
    }

    // =====================================================
    // MODIFICAR DISPONIBILIDAD DE CITAS
    // =====================================================

    @Transactional
    public DisponibilidadCita actualizar(
            Integer id,
            ActualizarDisponibilidadCitaDTO dto) {

        DisponibilidadCita cita = buscarPorId(id);

        validarDatos(
                dto.especialidadId(),
                dto.establecimientoId(),
                dto.fechaAtencion(),
                dto.horaInicio(),
                dto.horaFin(),
                dto.cantidadCupos(),
                false
        );

        Especialidad especialidad = especialidadRepository
                .findById(dto.especialidadId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "La especialidad indicada no existe."
                        )
                );

        if (!Boolean.TRUE.equals(especialidad.getActivo())) {
            throw new IllegalArgumentException(
                    "La especialidad seleccionada está inactiva."
            );
        }

        Establecimiento establecimiento = establecimientoRepository
                .findById(dto.establecimientoId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "El establecimiento indicado no existe."
                        )
                );

        if (!Boolean.TRUE.equals(establecimiento.getActivo())) {
            throw new IllegalArgumentException(
                    "El establecimiento seleccionado está inactivo."
            );
        }

        validarDuplicadoAlActualizar(
                id,
                dto.especialidadId(),
                dto.establecimientoId(),
                dto.fechaAtencion(),
                dto.horaInicio(),
                dto.horaFin()
        );

        String valorAnterior = describir(cita);

        cita.setEspecialidad(especialidad);
        cita.setEstablecimiento(establecimiento);
        cita.setFechaAtencion(dto.fechaAtencion());
        cita.setHoraInicio(dto.horaInicio());
        cita.setHoraFin(dto.horaFin());
        cita.setCantidadCupos(dto.cantidadCupos());
        cita.setFechaActualizacion(LocalDateTime.now());
        cita.setActualizadoPor(obtenerUsuarioAutenticado());

        // Si está inactiva, se conserva ese estado.
        // En los demás casos, se actualiza según los cupos.
        if (!"Inactivo".equalsIgnoreCase(cita.getEstado())) {
            cita.setEstado(
                    dto.cantidadCupos() > 0
                            ? "Disponible"
                            : "Agotado"
            );
        }

        DisponibilidadCita actualizada =
                disponibilidadRepository.save(cita);

        historialService.registrar(
                obtenerNombreUsuario(),
                "DisponibilidadCita",
                actualizada.getDisponibilidadCitaId(),
                "ACTUALIZAR",
                "Modificación de disponibilidad de citas.",
                valorAnterior,
                describir(actualizada)
        );

        return buscarPorId(actualizada.getDisponibilidadCitaId());
    }

    // =====================================================
    // CAMBIAR ESTADO DE UNA CITA
    // =====================================================

    @Transactional
    public DisponibilidadCita cambiarEstado(
            Integer id,
            CambiarEstadoCitaDTO dto) {

        if (dto == null || dto.estado() == null
                || dto.estado().isBlank()) {

            throw new IllegalArgumentException(
                    "Debe proporcionar el nuevo estado."
            );
        }

        DisponibilidadCita cita = buscarPorId(id);

        String nuevoEstado = dto.estado().trim();

        if (!nuevoEstado.equalsIgnoreCase("Disponible")
                && !nuevoEstado.equalsIgnoreCase("Agotado")
                && !nuevoEstado.equalsIgnoreCase("Inactivo")) {

            throw new IllegalArgumentException(
                    "Estado no válido. Utilice Disponible, Agotado o Inactivo."
            );
        }

        if (nuevoEstado.equalsIgnoreCase("Disponible")
                && cita.getCantidadCupos() <= 0) {

            throw new IllegalArgumentException(
                    "No se puede habilitar una cita sin cupos disponibles."
            );
        }

        if (nuevoEstado.equalsIgnoreCase("Agotado")
                && cita.getCantidadCupos() > 0) {

            throw new IllegalArgumentException(
                    "Para marcar una cita como agotada, primero debe establecer sus cupos en cero."
            );
        }

        String valorAnterior = describir(cita);

        if (nuevoEstado.equalsIgnoreCase("Disponible")) {
            cita.setEstado("Disponible");
        } else if (nuevoEstado.equalsIgnoreCase("Agotado")) {
            cita.setEstado("Agotado");
        } else {
            cita.setEstado("Inactivo");
        }

        cita.setFechaActualizacion(LocalDateTime.now());
        cita.setActualizadoPor(obtenerUsuarioAutenticado());

        DisponibilidadCita actualizada =
                disponibilidadRepository.save(cita);

        historialService.registrar(
                obtenerNombreUsuario(),
                "DisponibilidadCita",
                actualizada.getDisponibilidadCitaId(),
                "CAMBIAR_ESTADO",
                "Cambio de estado de disponibilidad de citas.",
                valorAnterior,
                describir(actualizada)
        );

        return buscarPorId(actualizada.getDisponibilidadCitaId());
    }

    // =====================================================
    // VALIDACIONES
    // =====================================================

    private void validarDatos(
            Integer especialidadId,
            Integer establecimientoId,
            LocalDate fecha,
            LocalTime horaInicio,
            LocalTime horaFin,
            Integer cantidadCupos,
            boolean esPublicacion) {

        if (especialidadId == null || especialidadId <= 0) {
            throw new IllegalArgumentException(
                    "Debe seleccionar una especialidad válida."
            );
        }

        if (establecimientoId == null || establecimientoId <= 0) {
            throw new IllegalArgumentException(
                    "Debe seleccionar un establecimiento válido."
            );
        }

        if (fecha == null) {
            throw new IllegalArgumentException(
                    "Debe indicar la fecha de atención."
            );
        }

        if (fecha.isBefore(LocalDate.now().plusDays(1))) {
            throw new IllegalArgumentException(
                    "La fecha de atención debe ser desde mañana en adelante."
            );
        }

        if (horaInicio == null || horaFin == null) {
            throw new IllegalArgumentException(
                    "Debe indicar la hora de inicio y fin."
            );
        }

        if (!horaInicio.isBefore(horaFin)) {
            throw new IllegalArgumentException(
                    "La hora de inicio debe ser anterior a la hora de fin."
            );
        }

        if (cantidadCupos == null || cantidadCupos < 0) {
            throw new IllegalArgumentException(
                    "La cantidad de cupos no puede ser negativa."
            );
        }

        if (esPublicacion && cantidadCupos == 0) {
            throw new IllegalArgumentException(
                    "Una publicación nueva debe tener al menos un cupo."
            );
        }
    }

    // =====================================================
    // VALIDAR DUPLICADOS AL PUBLICAR
    // =====================================================

    private void validarDuplicado(
            Integer especialidadId,
            Integer establecimientoId,
            LocalDate fecha,
            LocalTime horaInicio,
            LocalTime horaFin) {

        boolean existe = disponibilidadRepository.existeHorario(
                especialidadId,
                establecimientoId,
                fecha,
                horaInicio,
                horaFin
        );

        if (existe) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe una publicación con ese horario."
            );
        }
    }

    // =====================================================
    // VALIDAR DUPLICADOS AL ACTUALIZAR
    // =====================================================

    private void validarDuplicadoAlActualizar(
            Integer id,
            Integer especialidadId,
            Integer establecimientoId,
            LocalDate fecha,
            LocalTime horaInicio,
            LocalTime horaFin) {

        boolean existe = disponibilidadRepository.existeHorarioExceptoActual(
                especialidadId,
                establecimientoId,
                fecha,
                horaInicio,
                horaFin,
                id
        );

        if (existe) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe otra publicación con ese horario."
            );
        }
    }

    // =====================================================
    // USUARIO AUTENTICADO
    // =====================================================

    private String obtenerNombreUsuario() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication.getName() == null) {

            throw new IllegalStateException(
                    "No se pudo identificar al usuario autenticado."
            );
        }

        return authentication.getName();
    }

    private com.essalud.api.entity.Usuario obtenerUsuarioAutenticado() {

        String nombreUsuario = obtenerNombreUsuario();

        return historialService.obtenerUsuario(nombreUsuario);
    }

    // =====================================================
    // DESCRIPCIÓN PARA AUDITORÍA
    // =====================================================

    private String describir(DisponibilidadCita cita) {

        return "EspecialidadId: "
                + cita.getEspecialidad().getEspecialidadId()
                + "; EstablecimientoId: "
                + cita.getEstablecimiento().getEstablecimientoId()
                + "; Fecha: "
                + cita.getFechaAtencion()
                + "; Inicio: "
                + cita.getHoraInicio()
                + "; Fin: "
                + cita.getHoraFin()
                + "; Cupos: "
                + cita.getCantidadCupos()
                + "; Estado: "
                + cita.getEstado();
    }
}