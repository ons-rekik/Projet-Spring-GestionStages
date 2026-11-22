package com.iit.gestion_stages.dto;

import com.iit.gestion_stages.entity.enums.StatutEtape;
import jakarta.validation.constraints.NotNull;

public record AvancementDto(
        @NotNull(message = "Le statut est obligatoire") StatutEtape statut
) {}