package com.iit.gestion_stages.service;

import com.iit.gestion_stages.dto.DtoMapper;
import com.iit.gestion_stages.dto.LivrableCreateDto;
import com.iit.gestion_stages.dto.LivrableDto;
import com.iit.gestion_stages.entity.Livrable;
import com.iit.gestion_stages.entity.Stage;
import com.iit.gestion_stages.exception.ResourceNotFoundException;
import com.iit.gestion_stages.repository.LivrableRepository;
import com.iit.gestion_stages.repository.StageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LivrableService {

    private final LivrableRepository livrableRepository;
    private final StageRepository stageRepository;

    public List<LivrableDto> listerParStage(Long stageId) {
        if (!stageRepository.existsById(stageId)) {
            throw new ResourceNotFoundException("Stage", stageId);
        }
        return livrableRepository.findByStageId(stageId).stream().map(DtoMapper::toDto).toList();
    }

    @Transactional
    public LivrableDto deposer(Long stageId, LivrableCreateDto dto) {
        Stage stage = stageRepository.findById(stageId)
                .orElseThrow(() -> new ResourceNotFoundException("Stage", stageId));
        Livrable livrable = new Livrable();
        livrable.setStage(stage);
        livrable.setTitre(dto.titre());
        livrable.setLien(dto.lien());
        return DtoMapper.toDto(livrableRepository.save(livrable));
    }

    @Transactional
    public void supprimer(Long id) {
        Livrable livrable = livrableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livrable", id));
        livrableRepository.delete(livrable);
    }
}