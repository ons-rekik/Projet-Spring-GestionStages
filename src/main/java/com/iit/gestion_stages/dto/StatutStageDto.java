package com.iit.gestion_stages.dto;

import com.iit.gestion_stages.entity.enums.StatutStage;
import jakarta.validation.constraints.NotNull;

public record StatutStageDto(
        @NotNull(message = "Le statut est obligatoire") StatutStage statut
) {}