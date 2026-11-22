package com.iit.gestion_stages.service;

import com.iit.gestion_stages.dto.DtoMapper;
import com.iit.gestion_stages.dto.EtudiantCreateDto;
import com.iit.gestion_stages.dto.EtudiantDto;
import com.iit.gestion_stages.entity.Etudiant;
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
public class EtudiantService {

    private final EtudiantRepository etudiantRepository;
    private final EnseignantRepository enseignantRepository;
    private final StageRepository stageRepository;

    public List<EtudiantDto> lister() {
        return etudiantRepository.findAll().stream().map(DtoMapper::toDto).toList();
    }

    public List<EtudiantDto> listerParFiliere(String filiere) {
        return etudiantRepository.findByFiliere(filiere).stream().map(DtoMapper::toDto).toList();
    }

    public EtudiantDto obtenir(Long id) {
        return DtoMapper.toDto(trouver(id));
    }

    @Transactional
    public EtudiantDto creer(EtudiantCreateDto dto) {
        verifierEmailDisponible(dto.email(), null);
        Etudiant etudiant = new Etudiant();
        appliquer(dto, etudiant);
        return DtoMapper.toDto(etudiantRepository.save(etudiant));
    }

    @Transactional
    public EtudiantDto modifier(Long id, EtudiantCreateDto dto) {
        Etudiant etudiant = trouver(id);
        verifierEmailDisponible(dto.email(), id);
        appliquer(dto, etudiant);
        return DtoMapper.toDto(etudiantRepository.save(etudiant));
    }

    @Transactional
    public void supprimer(Long id) {
        Etudiant etudiant = trouver(id);
        if (!stageRepository.findByEtudiantId(id).isEmpty()) {
            throw new BusinessException("Impossible de supprimer cet étudiant : il a un ou plusieurs stages");
        }
        etudiantRepository.delete(etudiant);
    }

    private Etudiant trouver(Long id) {
        return etudiantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Étudiant", id));
    }

    private void verifierEmailDisponible(String email, Long idCourant) {
        boolean pris = etudiantRepository.findByEmail(email)
                .filter(e -> !e.getId().equals(idCourant))
                .isPresent()
                || enseignantRepository.findByEmail(email).isPresent();
        if (pris) {
            throw new BusinessException("Cet email est déjà utilisé : " + email);
        }
    }

    private void appliquer(EtudiantCreateDto dto, Etudiant etudiant) {
        etudiant.setNom(dto.nom());
        etudiant.setPrenom(dto.prenom());
        etudiant.setEmail(dto.email());
        if (dto.motDePasse() != null && !dto.motDePasse().isBlank()) {
            etudiant.setMotDePasse(dto.motDePasse());
        }
        etudiant.setMatricule(dto.matricule());
        etudiant.setFiliere(dto.filiere());
        etudiant.setNiveau(dto.niveau());
    }
}