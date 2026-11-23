package com.essalud.api.repository;

import com.essalud.api.entity.Establecimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstablecimientoRepository
        extends JpaRepository<Establecimiento, Integer> {

    List<Establecimiento> findByActivoTrueOrderByNombreAsc();

}