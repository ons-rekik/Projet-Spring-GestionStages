package com.iit.gestion_stages.dto;

import java.time.LocalDateTime;

public record ReunionDto(
        Long id,
        LocalDateTime date,
        String lieu,
        String compteRendu
) {}