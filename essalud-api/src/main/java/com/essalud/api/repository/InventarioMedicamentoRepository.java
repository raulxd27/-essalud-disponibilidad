package com.essalud.api.repository;

import com.essalud.api.entity.InventarioMedicamento;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventarioMedicamentoRepository
        extends JpaRepository<InventarioMedicamento, Integer> {

    // Consultar inventario de un establecimiento
    // Cargar Medicamento y Establecimiento en la misma consulta
    @EntityGraph(attributePaths = {"medicamento", "establecimiento"})
    List<InventarioMedicamento>
            findByEstablecimiento_EstablecimientoId(Integer establecimientoId);

    // Consultar inventario de un medicamento en un establecimiento
    @EntityGraph(attributePaths = {"medicamento", "establecimiento"})
    Optional<InventarioMedicamento>
            findByMedicamento_MedicamentoIdAndEstablecimiento_EstablecimientoId(
                    Integer medicamentoId,
                    Integer establecimientoId
            );
}
