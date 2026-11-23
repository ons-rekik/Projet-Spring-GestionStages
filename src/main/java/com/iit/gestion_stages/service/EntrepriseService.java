package com.iit.gestion_stages.service;

import com.iit.gestion_stages.dto.DtoMapper;
import com.iit.gestion_stages.dto.EntrepriseCreateDto;
import com.iit.gestion_stages.dto.EntrepriseDto;
import com.iit.gestion_stages.entity.Entreprise;
import com.iit.gestion_stages.exception.BusinessException;
import com.iit.gestion_stages.exception.ResourceNotFoundException;
import com.iit.gestion_stages.repository.EntrepriseRepository;
import com.iit.gestion_stages.repository.StageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EntrepriseService {

    private final EntrepriseRepository entrepriseRepository;
    private final StageRepository stageRepository;

    public List<EntrepriseDto> lister() {
        return entrepriseRepository.findAll().stream().map(DtoMapper::toDto).toList();
    }

    public List<EntrepriseDto> rechercherParNom(String nom) {
        return entrepriseRepository.findByNomContainingIgnoreCase(nom).stream().map(DtoMapper::toDto).toList();
    }

    public EntrepriseDto obtenir(Long id) {
        return DtoMapper.toDto(trouver(id));
    }

    @Transactional
    public EntrepriseDto creer(EntrepriseCreateDto dto) {
        Entreprise entreprise = new Entreprise();
        appliquer(dto, entreprise);
        return DtoMapper.toDto(entrepriseRepository.save(entreprise));
    }

    @Transactional
    public EntrepriseDto modifier(Long id, EntrepriseCreateDto dto) {
        Entreprise entreprise = trouver(id);
        appliquer(dto, entreprise);
        return DtoMapper.toDto(entrepriseRepository.save(entreprise));
    }

    @Transactional
    public void supprimer(Long id) {
        Entreprise entreprise = trouver(id);
        boolean utilisee = stageRepository.findAll().stream()
                .anyMatch(stage -> stage.getEntreprise().getId().equals(id));
        if (utilisee) {
            throw new BusinessException("Impossible de supprimer cette entreprise : elle est liée à un ou plusieurs stages");
        }
        entrepriseRepository.delete(entreprise);
    }

    private Entreprise trouver(Long id) {
        return entrepriseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entreprise", id));
    }

    private void appliquer(EntrepriseCreateDto dto, Entreprise entreprise) {
        entreprise.setNom(dto.nom());
        entreprise.setAdresse(dto.adresse());
        entreprise.setSecteur(dto.secteur());
        entreprise.setEmail(dto.email());
        entreprise.setTelephone(dto.telephone());
        entreprise.setTuteur(dto.tuteur());
    }
}