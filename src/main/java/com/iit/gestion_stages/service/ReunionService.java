package com.iit.gestion_stages.service;

import com.iit.gestion_stages.dto.DtoMapper;
import com.iit.gestion_stages.dto.ReunionCreateDto;
import com.iit.gestion_stages.dto.ReunionDto;
import com.iit.gestion_stages.entity.Reunion;
import com.iit.gestion_stages.entity.Stage;
import com.iit.gestion_stages.exception.ResourceNotFoundException;
import com.iit.gestion_stages.repository.ReunionRepository;
import com.iit.gestion_stages.repository.StageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReunionService {

    private final ReunionRepository reunionRepository;
    private final StageRepository stageRepository;

    public List<ReunionDto> listerParStage(Long stageId) {
        if (!stageRepository.existsById(stageId)) {
            throw new ResourceNotFoundException("Stage", stageId);
        }
        return reunionRepository.findByStageIdOrderByDateDesc(stageId).stream().map(DtoMapper::toDto).toList();
    }

    public ReunionDto obtenir(Long id) {
        return DtoMapper.toDto(trouver(id));
    }

    @Transactional
    public ReunionDto creer(Long stageId, ReunionCreateDto dto) {
        Stage stage = stageRepository.findById(stageId)
                .orElseThrow(() -> new ResourceNotFoundException("Stage", stageId));
        Reunion reunion = new Reunion();
        reunion.setStage(stage);
        appliquer(dto, reunion);
        return DtoMapper.toDto(reunionRepository.save(reunion));
    }

    @Transactional
    public ReunionDto modifier(Long id, ReunionCreateDto dto) {
        Reunion reunion = trouver(id);
        appliquer(dto, reunion);
        return DtoMapper.toDto(reunionRepository.save(reunion));
    }

    @Transactional
    public void supprimer(Long id) {
        reunionRepository.delete(trouver(id));
    }

    private Reunion trouver(Long id) {
        return reunionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Réunion", id));
    }

    private void appliquer(ReunionCreateDto dto, Reunion reunion) {
        reunion.setDate(dto.date());
        reunion.setLieu(dto.lieu());
        reunion.setCompteRendu(dto.compteRendu());
    }
}