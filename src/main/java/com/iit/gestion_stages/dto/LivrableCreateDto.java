package com.iit.gestion_stages.dto;

import jakarta.validation.constraints.NotBlank;

public record LivrableCreateDto(
        @NotBlank(message = "Le titre est obligatoire") String titre,
        String lien
) {}