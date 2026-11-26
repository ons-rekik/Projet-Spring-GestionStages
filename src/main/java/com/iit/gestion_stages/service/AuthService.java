package com.iit.gestion_stages.service;

import com.iit.gestion_stages.dto.ConnexionDto;
import com.iit.gestion_stages.dto.UtilisateurConnecteDto;
import com.iit.gestion_stages.entity.Utilisateur;
import com.iit.gestion_stages.entity.enums.Role;
import com.iit.gestion_stages.exception.AuthentificationException;
import com.iit.gestion_stages.repository.EnseignantRepository;
import com.iit.gestion_stages.repository.EtudiantRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class AuthService {

    private final EtudiantRepository etudiantRepository;
    private final EnseignantRepository enseignantRepository;
    private final String adminEmail;
    private final String adminMotDePasse;

    public AuthService(EtudiantRepository etudiantRepository,
                       EnseignantRepository enseignantRepository,
                       @Value("${app.admin.email}") String adminEmail,
                       @Value("${app.admin.password}") String adminMotDePasse) {
        this.etudiantRepository = etudiantRepository;
        this.enseignantRepository = enseignantRepository;
        this.adminEmail = adminEmail;
        this.adminMotDePasse = adminMotDePasse;
    }

    public UtilisateurConnecteDto connecter(ConnexionDto dto) {
        String email = dto.email().trim();

        if (email.equalsIgnoreCase(adminEmail)) {
            if (adminMotDePasse.equals(dto.motDePasse())) {
                return new UtilisateurConnecteDto(0L, "Administrateur", "IIT", adminEmail, Role.ADMIN);
            }
            throw refus();
        }

        Utilisateur utilisateur = chercher(email).orElseThrow(this::refus);
        String motDePasse = utilisateur.getMotDePasse();
        if (motDePasse == null || motDePasse.isBlank() || !motDePasse.equals(dto.motDePasse())) {
            throw refus();
        }
        return new UtilisateurConnecteDto(utilisateur.getId(), utilisateur.getNom(),
                utilisateur.getPrenom(), utilisateur.getEmail(), utilisateur.getRole());
    }

    private Optional<Utilisateur> chercher(String email) {
        Optional<Utilisateur> etudiant = etudiantRepository.findByEmail(email).map(e -> (Utilisateur) e);
        if (etudiant.isPresent()) {
            return etudiant;
        }
        return enseignantRepository.findByEmail(email).map(e -> (Utilisateur) e);
    }

    private AuthentificationException refus() {
        return new AuthentificationException("Email ou mot de passe incorrect");
    }
}