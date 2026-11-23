package com.iit.gestion_stages.service;

import com.iit.gestion_stages.dto.StatistiquesDto;
import com.iit.gestion_stages.entity.enums.StatutStage;
import com.iit.gestion_stages.repository.EnseignantRepository;
import com.iit.gestion_stages.repository.EntrepriseRepository;
import com.iit.gestion_stages.repository.EtudiantRepository;
import com.iit.gestion_stages.repository.StageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatistiquesService {

    private final StageRepository stageRepository;
    private final EtudiantRepository etudiantRepository;
    private final EnseignantRepository enseignantRepository;
    private final EntrepriseRepository entrepriseRepository;

    public StatistiquesDto obtenir() {
        Map<StatutStage, Long> parStatut = new EnumMap<>(StatutStage.class);
        for (StatutStage statut : StatutStage.values()) {
            parStatut.put(statut, stageRepository.countByStatut(statut));
        }
        return new StatistiquesDto(
                stageRepository.count(),
                parStatut,
                stageRepository.findByEncadrantIsNull().size(),
                etudiantRepository.count(),
                enseignantRepository.count(),
                entrepriseRepository.count());
    }
}