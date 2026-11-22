package com.iit.gestion_stages.dto;

import jakarta.validation.constraints.NotNull;

public record AffectationDto(
        @NotNull(message = "L'enseignant est obligatoire") Long enseignantId
) {}