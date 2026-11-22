package com.iit.gestion_stages.dto;

import com.iit.gestion_stages.entity.enums.StatutStage;
import java.util.Map;

public record StatistiquesDto(
        long totalStages,
        Map<StatutStage, Long> stagesParStatut,
        long stagesSansEncadrant,
        long totalEtudiants,
        long totalEnseignants,
        long totalEntreprises
) {}