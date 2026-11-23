package com.essalud.api.repository;

import com.essalud.api.entity.HistorialActualizacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistorialActualizacionRepository
        extends JpaRepository<HistorialActualizacion, Long> {

}