package com.essalud.api.controller;

import com.essalud.api.dto.ActualizarDisponibilidadCitaDTO;
import com.essalud.api.dto.CambiarEstadoCitaDTO;
import com.essalud.api.dto.CrearDisponibilidadCitaDTO;
import com.essalud.api.dto.DisponibilidadCitaDTO;
import com.essalud.api.entity.DisponibilidadCita;
import com.essalud.api.service.DisponibilidadCitaService;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/citas")
public class DisponibilidadCitaController {

    private final DisponibilidadCitaService disponibilidadCitaService;

    public DisponibilidadCitaController(
            DisponibilidadCitaService disponibilidadCitaService) {

        this.disponibilidadCitaService = disponibilidadCitaService;
    }

    // =====================================================
    // CONSULTAR DISPONIBILIDAD DE CITAS
    // =====================================================

    @GetMapping
    public List<DisponibilidadCitaDTO> consultarCitas(

            @RequestParam(required = false)
            Integer especialidadId,

            @RequestParam(required = false)
            Integer establecimientoId,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fecha) {

        List<DisponibilidadCita> citas;

        if (especialidadId != null
                && establecimientoId != null
                && fecha != null) {

            citas = disponibilidadCitaService.buscarDisponibilidad(
                    especialidadId,
                    establecimientoId,
                    fecha
            );

        } else if (establecimientoId != null && fecha != null) {

            citas = disponibilidadCitaService
                    .listarPorEstablecimientoYFecha(
                            establecimientoId,
                            fecha
                    );

        } else if (especialidadId != null) {

            citas = disponibilidadCitaService
                    .listarPorEspecialidad(especialidadId);

        } else {

            throw new IllegalArgumentException(
                    "Debe proporcionar al menos el ID de la especialidad "
                    + "o el establecimiento y la fecha."
            );
        }

        return citas.stream()
                .map(this::convertirDTO)
                .toList();
    }

    // =====================================================
    // PUBLICAR DISPONIBILIDAD DE CITAS
    // =====================================================

    @PostMapping
    public DisponibilidadCitaDTO publicarCitas(
            @RequestBody CrearDisponibilidadCitaDTO dto) {

        DisponibilidadCita cita =
                disponibilidadCitaService.publicar(dto);

        return convertirDTO(cita);
    }

    // =====================================================
    // MODIFICAR DISPONIBILIDAD DE CITAS
    // =====================================================

    @PutMapping("/{id}")
    public DisponibilidadCitaDTO actualizarCitas(
            @PathVariable Integer id,
            @RequestBody ActualizarDisponibilidadCitaDTO dto) {

        DisponibilidadCita cita =
                disponibilidadCitaService.actualizar(id, dto);

        return convertirDTO(cita);
    }

    // =====================================================
    // CAMBIAR ESTADO DE UNA CITA
    // =====================================================

    @PatchMapping("/{id}/estado")
    public DisponibilidadCitaDTO cambiarEstado(
            @PathVariable Integer id,
            @RequestBody CambiarEstadoCitaDTO dto) {

        DisponibilidadCita cita =
                disponibilidadCitaService.cambiarEstado(id, dto);

        return convertirDTO(cita);
    }

    // =====================================================
    // CONVERTIR ENTIDAD A DTO
    // =====================================================

    private DisponibilidadCitaDTO convertirDTO(
            DisponibilidadCita cita) {

        return new DisponibilidadCitaDTO(
                cita.getDisponibilidadCitaId(),
                cita.getEspecialidad().getEspecialidadId(),
                cita.getEspecialidad().getNombre(),
                cita.getEstablecimiento().getEstablecimientoId(),
                cita.getEstablecimiento().getNombre(),
                cita.getFechaAtencion(),
                cita.getHoraInicio(),
                cita.getHoraFin(),
                cita.getCantidadCupos(),
                cita.getEstado(),
                cita.getFechaActualizacion()
        );
    }
}