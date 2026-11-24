package com.iit.gestion_stages.controller.mvc.form;

import com.iit.gestion_stages.dto.ObservationCreateDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Objet de formulaire SSR pour les observations et recommandations de l'encadrant.
 */
@Getter
@Setter
@NoArgsConstructor
public class ObservationForm {

    @NotBlank(message = "Le contenu de l'observation est obligatoire")
    @Size(max = 2000, message = "L'observation ne doit pas dépasser 2000 caractères")
    private String contenu;

    @Size(max = 2000, message = "La recommandation ne doit pas dépasser 2000 caractères")
    private String recommandation;

    /** Formulaire -> DTO attendu par ObservationService.ajouter(). L'auteur est l'encadrant du stage. */
    public ObservationCreateDto toCreateDto(Long auteurId) {
        String reco = (recommandation == null || recommandation.isBlank()) ? null : recommandation.trim();
        return new ObservationCreateDto(contenu.trim(), reco, auteurId);
    }
}