package com.iit.gestion_stages.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record ReunionCreateDto(
        @NotNull(message = "La date est obligatoire") LocalDateTime date,
        String lieu,
        String compteRendu
) {}