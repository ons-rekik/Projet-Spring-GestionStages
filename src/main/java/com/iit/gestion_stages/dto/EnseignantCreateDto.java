package com.iit.gestion_stages.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EnseignantCreateDto(
        @NotBlank(message = "Le nom est obligatoire") String nom,
        @NotBlank(message = "Le prénom est obligatoire") String prenom,
        @NotBlank(message = "L'email est obligatoire") @Email(message = "Email invalide") String email,
        String motDePasse,
        String specialite,
        String departement
) {}