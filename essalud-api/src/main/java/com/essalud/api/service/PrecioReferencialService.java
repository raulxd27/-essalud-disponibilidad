package com.essalud.api.service;

import com.essalud.api.entity.PrecioReferencial;
import com.essalud.api.repository.PrecioReferencialRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrecioReferencialService {

    private final PrecioReferencialRepository precioReferencialRepository;

    public PrecioReferencialService(
            PrecioReferencialRepository precioReferencialRepository) {
        this.precioReferencialRepository = precioReferencialRepository;
    }

    // Listar todos los precios referenciales
    public List<PrecioReferencial> listarTodos() {
        return precioReferencialRepository.findAll();
    }

    // Buscar precio referencial por ID
    public PrecioReferencial buscarPorId(Integer id) {
        return precioReferencialRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Precio referencial no encontrado: " + id));
    }
}