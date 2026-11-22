package com.iit.gestion_stages.dto;

import jakarta.validation.constraints.NotBlank;

public record ObservationCreateDto(
        @NotBlank(message = "Le contenu est obligatoire") String contenu,
        String recommandation,
        Long auteurId
) {}