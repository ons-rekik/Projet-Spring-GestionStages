package com.iit.gestion_stages.dto;

import java.time.LocalDate;

public record ObservationDto(
        Long id,
        String contenu,
        String recommandation,
        LocalDate date,
        Long auteurId,
        String auteurNomComplet
) {}