package com.iit.gestion_stages.service;

import com.iit.gestion_stages.dto.DtoMapper;
import com.iit.gestion_stages.dto.EnseignantCreateDto;
import com.iit.gestion_stages.dto.EnseignantDto;
import com.iit.gestion_stages.entity.Enseignant;
import com.iit.gestion_stages.exception.BusinessException;
import com.iit.gestion_stages.exception.ResourceNotFoundException;
import com.iit.gestion_stages.repository.EnseignantRepository;
import com.iit.gestion_stages.repository.EtudiantRepository;
import com.iit.gestion_stages.repository.StageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EnseignantService {

    private final EnseignantRepository enseignantRepository;
    private final EtudiantRepository etudiantRepository;
    private final StageRepository stageRepository;

    public List<EnseignantDto> lister() {
        return enseignantRepository.findAll().stream().map(DtoMapper::toDto).toList();
    }

    public EnseignantDto obtenir(Long id) {
        return DtoMapper.toDto(trouver(id));
    }

    @Transactional
    public EnseignantDto creer(EnseignantCreateDto dto) {
        verifierEmailDisponible(dto.email(), null);
        Enseignant enseignant = new Enseignant();
        appliquer(dto, enseignant);
        return DtoMapper.toDto(enseignantRepository.save(enseignant));
    }

    @Transactional
    public EnseignantDto modifier(Long id, EnseignantCreateDto dto) {
        Enseignant enseignant = trouver(id);
        verifierEmailDisponible(dto.email(), id);
        appliquer(dto, enseignant);
        return DtoMapper.toDto(enseignantRepository.save(enseignant));
    }

    @Transactional
    public void supprimer(Long id) {
        Enseignant enseignant = trouver(id);
        if (!stageRepository.findByEncadrantId(id).isEmpty()) {
            throw new BusinessException("Impossible de supprimer cet enseignant : il encadre un ou plusieurs stages");
        }
        enseignantRepository.delete(enseignant);
    }

    private Enseignant trouver(Long id) {
        return enseignantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Enseignant", id));
    }

    private void verifierEmailDisponible(String email, Long idCourant) {
        boolean pris = enseignantRepository.findByEmail(email)
                .filter(e -> !e.getId().equals(idCourant))
                .isPresent()
                || etudiantRepository.findByEmail(email).isPresent();
        if (pris) {
            throw new BusinessException("Cet email est déjà utilisé : " + email);
        }
    }

    private void appliquer(EnseignantCreateDto dto, Enseignant enseignant) {
        enseignant.setNom(dto.nom());
        enseignant.setPrenom(dto.prenom());
        enseignant.setEmail(dto.email());
        if (dto.motDePasse() != null && !dto.motDePasse().isBlank()) {
            enseignant.setMotDePasse(dto.motDePasse());
        }
        enseignant.setSpecialite(dto.specialite());
        enseignant.setDepartement(dto.departement());
    }
}