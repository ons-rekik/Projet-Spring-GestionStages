package com.iit.gestion_stages.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record StageCreateDto(
        @NotBlank(message = "Le sujet est obligatoire") String sujet,
        LocalDate dateDebut,
        LocalDate dateFin,
        @NotNull(message = "L'étudiant est obligatoire") Long etudiantId,
        @NotNull(message = "L'entreprise est obligatoire") Long entrepriseId
) {}