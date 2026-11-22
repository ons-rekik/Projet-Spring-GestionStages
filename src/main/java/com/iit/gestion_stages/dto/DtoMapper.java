package com.iit.gestion_stages.dto;

import com.iit.gestion_stages.entity.*;

public final class DtoMapper {

    private DtoMapper() {}

    private static String nomComplet(Utilisateur u) {
        return u == null ? null : u.getPrenom() + " " + u.getNom();
    }

    public static EtudiantDto toDto(Etudiant e) {
        return new EtudiantDto(e.getId(), e.getNom(), e.getPrenom(), e.getEmail(),
                e.getRole(), e.getMatricule(), e.getFiliere(), e.getNiveau());
    }

    public static EnseignantDto toDto(Enseignant e) {
        return new EnseignantDto(e.getId(), e.getNom(), e.getPrenom(), e.getEmail(),
                e.getRole(), e.getSpecialite(), e.getDepartement());
    }

    public static EntrepriseDto toDto(Entreprise e) {
        return new EntrepriseDto(e.getId(), e.getNom(), e.getAdresse(), e.getSecteur(),
                e.getEmail(), e.getTelephone(), e.getTuteur());
    }

    public static StageDto toDto(Stage s) {
        Etudiant etudiant = s.getEtudiant();
        Enseignant encadrant = s.getEncadrant();
        Entreprise entreprise = s.getEntreprise();
        return new StageDto(
                s.getId(), s.getSujet(), s.getDateDebut(), s.getDateFin(), s.getStatut(),
                etudiant.getId(), nomComplet(etudiant),
                encadrant == null ? null : encadrant.getId(), nomComplet(encadrant),
                entreprise.getId(), entreprise.getNom());
    }

    public static ReunionDto toDto(Reunion r) {
        return new ReunionDto(r.getId(), r.getDate(), r.getLieu(), r.getCompteRendu());
    }

    public static ObservationDto toDto(Observation o) {
        Enseignant auteur = o.getAuteur();
        return new ObservationDto(o.getId(), o.getContenu(), o.getRecommandation(), o.getDate(),
                auteur == null ? null : auteur.getId(), nomComplet(auteur));
    }

    public static EtapeDto toDto(EtapeProgression e) {
        return new EtapeDto(e.getId(), e.getTitre(), e.getDescription(), e.getStatut(), e.isValidee());
    }

    public static LivrableDto toDto(Livrable l) {
        return new LivrableDto(l.getId(), l.getTitre(), l.getLien(), l.getDateDepot());
    }
}