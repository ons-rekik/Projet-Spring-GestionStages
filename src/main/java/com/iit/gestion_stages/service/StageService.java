package com.iit.gestion_stages.service;

import com.iit.gestion_stages.dto.AffectationDto;
import com.iit.gestion_stages.dto.DtoMapper;
import com.iit.gestion_stages.dto.StageCreateDto;
import com.iit.gestion_stages.dto.StageDto;
import com.iit.gestion_stages.dto.StatutStageDto;
import com.iit.gestion_stages.entity.Enseignant;
import com.iit.gestion_stages.entity.Entreprise;
import com.iit.gestion_stages.entity.Etudiant;
import com.iit.gestion_stages.entity.Stage;
import com.iit.gestion_stages.entity.enums.StatutStage;
import com.iit.gestion_stages.exception.BusinessException;
import com.iit.gestion_stages.exception.ResourceNotFoundException;
import com.iit.gestion_stages.repository.EnseignantRepository;
import com.iit.gestion_stages.repository.EntrepriseRepository;
import com.iit.gestion_stages.repository.EtudiantRepository;
import com.iit.gestion_stages.repository.StageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StageService {

    private final StageRepository stageRepository;
    private final EtudiantRepository etudiantRepository;
    private final EnseignantRepository enseignantRepository;
    private final EntrepriseRepository entrepriseRepository;

    public List<StageDto> lister() {
        return stageRepository.findAll().stream().map(DtoMapper::toDto).toList();
    }

    public List<StageDto> listerParStatut(StatutStage statut) {
        return stageRepository.findByStatut(statut).stream().map(DtoMapper::toDto).toList();
    }

    public List<StageDto> listerParEtudiant(Long etudiantId) {
        return stageRepository.findByEtudiantId(etudiantId).stream().map(DtoMapper::toDto).toList();
    }

    public List<StageDto> listerParEncadrant(Long enseignantId) {
        return stageRepository.findByEncadrantId(enseignantId).stream().map(DtoMapper::toDto).toList();
    }

    public List<StageDto> listerSansEncadrant() {
        return stageRepository.findByEncadrantIsNull().stream().map(DtoMapper::toDto).toList();
    }

    public StageDto obtenir(Long id) {
        return DtoMapper.toDto(trouver(id));
    }

    @Transactional
    public StageDto creer(StageCreateDto dto) {
        verifierDates(dto);
        Stage stage = new Stage();
        appliquer(dto, stage);
        return DtoMapper.toDto(stageRepository.save(stage));
    }

    @Transactional
    public StageDto modifier(Long id, StageCreateDto dto) {
        verifierDates(dto);
        Stage stage = trouver(id);
        appliquer(dto, stage);
        return DtoMapper.toDto(stageRepository.save(stage));
    }

    @Transactional
    public StageDto affecterEncadrant(Long stageId, AffectationDto dto) {
        Stage stage = trouver(stageId);
        Enseignant enseignant = enseignantRepository.findById(dto.enseignantId())
                .orElseThrow(() -> new ResourceNotFoundException("Enseignant", dto.enseignantId()));
        stage.setEncadrant(enseignant);
        return DtoMapper.toDto(stageRepository.save(stage));
    }

    @Transactional
    public StageDto changerStatut(Long stageId, StatutStageDto dto) {
        Stage stage = trouver(stageId);
        if (dto.statut() != StatutStage.EN_ATTENTE && stage.getEncadrant() == null) {
            throw new BusinessException("Impossible de changer le statut : aucun encadrant n'est affecté à ce stage");
        }
        stage.setStatut(dto.statut());
        return DtoMapper.toDto(stageRepository.save(stage));
    }

    @Transactional
    public void supprimer(Long id) {
        stageRepository.delete(trouver(id));
    }

    private Stage trouver(Long id) {
        return stageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stage", id));
    }

    private void verifierDates(StageCreateDto dto) {
        if (dto.dateDebut() != null && dto.dateFin() != null && dto.dateFin().isBefore(dto.dateDebut())) {
            throw new BusinessException("La date de fin doit être postérieure à la date de début");
        }
    }

    private void appliquer(StageCreateDto dto, Stage stage) {
        Etudiant etudiant = etudiantRepository.findById(dto.etudiantId())
                .orElseThrow(() -> new ResourceNotFoundException("Étudiant", dto.etudiantId()));
        Entreprise entreprise = entrepriseRepository.findById(dto.entrepriseId())
                .orElseThrow(() -> new ResourceNotFoundException("Entreprise", dto.entrepriseId()));
        stage.setSujet(dto.sujet());
        stage.setDateDebut(dto.dateDebut());
        stage.setDateFin(dto.dateFin());
        stage.setEtudiant(etudiant);
        stage.setEntreprise(entreprise);
    }
}