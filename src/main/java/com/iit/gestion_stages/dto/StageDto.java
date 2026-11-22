package com.iit.gestion_stages.dto;

import com.iit.gestion_stages.entity.enums.StatutStage;
import java.time.LocalDate;

public record StageDto(
        Long id,
        String sujet,
        LocalDate dateDebut,
        LocalDate dateFin,
        StatutStage statut,
        Long etudiantId,
        String etudiantNomComplet,
        Long encadrantId,
        String encadrantNomComplet,
        Long entrepriseId,
        String entrepriseNom
) {}