package com.iit.gestion_stages.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EntrepriseCreateDto(
        @NotBlank(message = "Le nom est obligatoire") String nom,
        String adresse,
        String secteur,
        @Email(message = "Email invalide") String email,
        String telephone,
        String tuteur
) {}