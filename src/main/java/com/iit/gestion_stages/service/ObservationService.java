package com.iit.gestion_stages.service;

import com.iit.gestion_stages.dto.DtoMapper;
import com.iit.gestion_stages.dto.ObservationCreateDto;
import com.iit.gestion_stages.dto.ObservationDto;
import com.iit.gestion_stages.entity.Enseignant;
import com.iit.gestion_stages.entity.Observation;
import com.iit.gestion_stages.entity.Stage;
import com.iit.gestion_stages.exception.BusinessException;
import com.iit.gestion_stages.exception.ResourceNotFoundException;
import com.iit.gestion_stages.repository.EnseignantRepository;
import com.iit.gestion_stages.repository.ObservationRepository;
import com.iit.gestion_stages.repository.StageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ObservationService {

    private final ObservationRepository observationRepository;
    private final StageRepository stageRepository;
    private final EnseignantRepository enseignantRepository;

    public List<ObservationDto> listerParStage(Long stageId) {
        if (!stageRepository.existsById(stageId)) {
            throw new ResourceNotFoundException("Stage", stageId);
        }
        return observationRepository.findByStageId(stageId).stream().map(DtoMapper::toDto).toList();
    }

    @Transactional
    public ObservationDto ajouter(Long stageId, ObservationCreateDto dto) {
        Stage stage = stageRepository.findById(stageId)
                .orElseThrow(() -> new ResourceNotFoundException("Stage", stageId));

        Observation observation = new Observation();
        observation.setStage(stage);
        observation.setContenu(dto.contenu());
        observation.setRecommandation(dto.recommandation());

        if (dto.auteurId() != null) {
            Enseignant auteur = enseignantRepository.findById(dto.auteurId())
                    .orElseThrow(() -> new ResourceNotFoundException("Enseignant", dto.auteurId()));
            Enseignant encadrant = stage.getEncadrant();
            if (encadrant != null && !encadrant.getId().equals(auteur.getId())) {
                throw new BusinessException("Seul l'enseignant encadrant peut ajouter une observation sur ce stage");
            }
            observation.setAuteur(auteur);
        }
        return DtoMapper.toDto(observationRepository.save(observation));
    }

    @Transactional
    public void supprimer(Long id) {
        Observation observation = observationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Observation", id));
        observationRepository.delete(observation);
    }
}