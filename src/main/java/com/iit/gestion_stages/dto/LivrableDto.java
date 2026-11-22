package com.iit.gestion_stages.dto;

import java.time.LocalDate;

public record LivrableDto(
        Long id,
        String titre,
        String lien,
        LocalDate dateDepot
) {}