package com.essalud.api.repository;

import com.essalud.api.entity.DisponibilidadCita;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DisponibilidadCitaRepository
        extends JpaRepository<DisponibilidadCita, Integer> {

    // Consultar una cita por ID cargando sus relaciones
    @Override
    @EntityGraph(attributePaths = {"especialidad", "establecimiento"})
    Optional<DisponibilidadCita> findById(Integer id);

    // Consultar citas disponibles por especialidad y desde una fecha
    @EntityGraph(attributePaths = {"especialidad", "establecimiento"})
    List<DisponibilidadCita>
            findByEspecialidad_EspecialidadIdAndEstadoIgnoreCaseAndCantidadCuposGreaterThanAndFechaAtencionGreaterThanEqual(
                    Integer especialidadId,
                    String estado,
                    Integer cantidadCupos,
                    LocalDate fechaActual
            );

    // Consultar citas por establecimiento, fecha y disponibilidad
    @EntityGraph(attributePaths = {"especialidad", "establecimiento"})
    List<DisponibilidadCita>
            findByEstablecimiento_EstablecimientoIdAndFechaAtencionAndEstadoIgnoreCaseAndCantidadCuposGreaterThanAndFechaAtencionGreaterThanEqual(
                    Integer establecimientoId,
                    LocalDate fecha,
                    String estado,
                    Integer cantidadCupos,
                    LocalDate fechaActual
            );

    // Consultar citas por especialidad, establecimiento y fecha
    @EntityGraph(attributePaths = {"especialidad", "establecimiento"})
    List<DisponibilidadCita>
            findByEspecialidad_EspecialidadIdAndEstablecimiento_EstablecimientoIdAndFechaAtencionAndEstadoIgnoreCaseAndCantidadCuposGreaterThanAndFechaAtencionGreaterThanEqual(
                    Integer especialidadId,
                    Integer establecimientoId,
                    LocalDate fecha,
                    String estado,
                    Integer cantidadCupos,
                    LocalDate fechaActual
            );

    // Comprobar si ya existe un horario idéntico al publicar
    // Se convierte explícitamente la hora a tipo TIME para SQL Server.
    @Query(value = """
            SELECT CASE WHEN EXISTS (
                SELECT 1
                FROM dbo.DisponibilidadCitas
                WHERE EspecialidadId = :especialidadId
                  AND EstablecimientoId = :establecimientoId
                  AND FechaAtencion = :fechaAtencion
                  AND HoraInicio = CAST(:horaInicio AS time)
                  AND HoraFin = CAST(:horaFin AS time)
            )
            THEN CAST(1 AS bit)
            ELSE CAST(0 AS bit)
            END
            """, nativeQuery = true)
    boolean existeHorario(
            @Param("especialidadId") Integer especialidadId,
            @Param("establecimientoId") Integer establecimientoId,
            @Param("fechaAtencion") LocalDate fechaAtencion,
            @Param("horaInicio") LocalTime horaInicio,
            @Param("horaFin") LocalTime horaFin
    );

    // Comprobar duplicados al modificar, excluyendo el registro actual
    @Query(value = """
            SELECT CASE WHEN EXISTS (
                SELECT 1
                FROM dbo.DisponibilidadCitas
                WHERE EspecialidadId = :especialidadId
                  AND EstablecimientoId = :establecimientoId
                  AND FechaAtencion = :fechaAtencion
                  AND HoraInicio = CAST(:horaInicio AS time)
                  AND HoraFin = CAST(:horaFin AS time)
                  AND DisponibilidadCitaId <> :disponibilidadCitaId
            )
            THEN CAST(1 AS bit)
            ELSE CAST(0 AS bit)
            END
            """, nativeQuery = true)
    boolean existeHorarioExceptoActual(
            @Param("especialidadId") Integer especialidadId,
            @Param("establecimientoId") Integer establecimientoId,
            @Param("fechaAtencion") LocalDate fechaAtencion,
            @Param("horaInicio") LocalTime horaInicio,
            @Param("horaFin") LocalTime horaFin,
            @Param("disponibilidadCitaId") Integer disponibilidadCitaId
    );
}