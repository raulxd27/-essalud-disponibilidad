package com.essalud.api.repository;

import com.essalud.api.entity.PrecioReferencial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrecioReferencialRepository
        extends JpaRepository<PrecioReferencial, Integer> {

}