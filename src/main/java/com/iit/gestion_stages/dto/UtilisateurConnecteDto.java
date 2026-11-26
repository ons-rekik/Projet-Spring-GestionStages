package com.iit.gestion_stages.dto;

import com.iit.gestion_stages.entity.enums.Role;

public record UtilisateurConnecteDto(
        Long id,
        String nom,
        String prenom,
        String email,
        Role role
) {}