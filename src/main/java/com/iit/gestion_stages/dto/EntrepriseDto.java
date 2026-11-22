package com.iit.gestion_stages.dto;

public record EntrepriseDto(
        Long id,
        String nom,
        String adresse,
        String secteur,
        String email,
        String telephone,
        String tuteur
) {}