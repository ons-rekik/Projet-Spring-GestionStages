package com.iit.gestion_stages.service;

import com.iit.gestion_stages.dto.AvancementDto;
import com.iit.gestion_stages.dto.DtoMapper;
import com.iit.gestion_stages.dto.EtapeCreateDto;
import com.iit.gestion_stages.dto.EtapeDto;
import com.iit.gestion_stages.entity.EtapeProgression;
import com.iit.gestion_stages.entity.Stage;
import com.iit.gestion_stages.entity.enums.StatutEtape;
import com.iit.gestion_stages.exception.BusinessException;
import com.iit.gestion_stages.exception.ResourceNotFoundException;
import com.iit.gestion_stages.repository.EtapeProgressionRepository;
import com.iit.gestion_stages.repository.StageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EtapeService {

    private final EtapeProgressionRepository etapeRepository;
    private final StageRepository stageRepository;

    public List<EtapeDto> listerParStage(Long stageId) {
        if (!stageRepository.existsById(stageId)) {
            throw new ResourceNotFoundException("Stage", stageId);
        }
        return etapeRepository.findByStageId(stageId).stream().map(DtoMapper::toDto).toList();
    }

    @Transactional
    public EtapeDto creer(Long stageId, EtapeCreateDto dto) {
        Stage stage = stageRepository.findById(stageId)
                .orElseThrow(() -> new ResourceNotFoundException("Stage", stageId));
        EtapeProgression etape = new EtapeProgression();
        etape.setStage(stage);
        etape.setTitre(dto.titre());
        etape.setDescription(dto.description());
        return DtoMapper.toDto(etapeRepository.save(etape));
    }

    @Transactional
    public EtapeDto declarerAvancement(Long etapeId, AvancementDto dto) {
        EtapeProgression etape = trouver(etapeId);
        if (etape.isValidee()) {
            throw new BusinessException("Cette étape est déjà validée par l'encadrant : modification impossible");
        }
        if (dto.statut() == StatutEtape.VALIDEE) {
            throw new BusinessException("Seul l'enseignant encadrant peut valider une étape");
        }
        etape.setStatut(dto.statut());
        return DtoMapper.toDto(etapeRepository.save(etape));
    }

    @Transactional
    public EtapeDto valider(Long etapeId) {
        EtapeProgression etape = trouver(etapeId);
        if (etape.isValidee()) {
            throw new BusinessException("Cette étape est déjà validée");
        }
        if (etape.getStatut() != StatutEtape.TERMINEE) {
            throw new BusinessException("Seule une étape terminée peut être validée");
        }
        etape.setStatut(StatutEtape.VALIDEE);
        etape.setValidee(true);
        return DtoMapper.toDto(etapeRepository.save(etape));
    }

    @Transactional
    public void supprimer(Long etapeId) {
        etapeRepository.delete(trouver(etapeId));
    }

    private EtapeProgression trouver(Long id) {
        return etapeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Étape", id));
    }
}