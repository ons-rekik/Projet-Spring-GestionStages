package com.iit.gestion_stages.dto;

import jakarta.validation.constraints.NotBlank;

public record ConnexionDto(
        @NotBlank(message = "L'email est obligatoire") String email,
        @NotBlank(message = "Le mot de passe est obligatoire") String motDePasse
) {}