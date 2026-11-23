package com.essalud.api.repository;

import com.essalud.api.entity.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EspecialidadRepository
        extends JpaRepository<Especialidad, Integer> {

    List<Especialidad> findByActivoTrueOrderByNombreAsc();

}