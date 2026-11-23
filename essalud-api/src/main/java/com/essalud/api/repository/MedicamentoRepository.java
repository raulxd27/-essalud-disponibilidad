package com.essalud.api.repository;

import com.essalud.api.entity.Medicamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicamentoRepository
        extends JpaRepository<Medicamento, Integer> {

    // Listar medicamentos activos
    List<Medicamento> findByActivoTrueOrderByNombreAsc();

    // Buscar medicamentos por nombre
    List<Medicamento> findByNombreContainingIgnoreCaseAndActivoTrue(
            String nombre
    );

}