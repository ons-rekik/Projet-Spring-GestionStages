package com.iit.gestion_stages.controller.mvc.form;

import com.iit.gestion_stages.dto.EtapeCreateDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Objet de formulaire SSR pour l'ajout d'une étape de progression.
 */
@Getter
@Setter
@NoArgsConstructor
public class EtapeForm {

    @NotBlank(message = "Le titre est obligatoire")
    private String titre;

    @Size(max = 1000, message = "La description ne doit pas dépasser 1000 caractères")
    private String description;

    /** Formulaire -> DTO attendu par EtapeService.creer(). */
    public EtapeCreateDto toCreateDto() {
        String desc = (description == null || description.isBlank()) ? null : description.trim();
        return new EtapeCreateDto(titre.trim(), desc);
    }
}