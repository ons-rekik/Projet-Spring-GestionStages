package com.iit.gestion_stages.dto;

import com.iit.gestion_stages.entity.enums.StatutEtape;

public record EtapeDto(
        Long id,
        String titre,
        String description,
        StatutEtape statut,
        boolean validee
) {}