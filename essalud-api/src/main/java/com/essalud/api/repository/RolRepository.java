package com.essalud.api.repository;

import com.essalud.api.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolRepository
        extends JpaRepository<Rol, Integer> {

}