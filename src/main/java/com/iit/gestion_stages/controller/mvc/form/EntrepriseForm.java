package com.iit.gestion_stages.controller.mvc.form;

import com.iit.gestion_stages.dto.EntrepriseCreateDto;
import com.iit.gestion_stages.dto.EntrepriseDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Objet de formulaire SSR pour les entreprises.
 * Les DTO du projet sont des records (sans getters/setters JavaBean) : Thymeleaf
 * (th:field) a besoin d'un objet modifiable pour pré-remplir et lier le formulaire.
 * Cette classe fait le pont entre le formulaire et les DTO utilisés par EntrepriseService.
 */
@Getter
@Setter
@NoArgsConstructor
public class EntrepriseForm {

    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    private String adresse;
    private String secteur;

    @Email(message = "Email invalide")
    private String email;

    private String telephone;
    private String tuteur;

    /** EntrepriseDto (retourné par le service) -> formulaire d'édition. */
    public static EntrepriseForm from(EntrepriseDto dto) {
        EntrepriseForm form = new EntrepriseForm();
        form.setId(dto.id());
        form.setNom(dto.nom());
        form.setAdresse(dto.adresse());
        form.setSecteur(dto.secteur());
        form.setEmail(dto.email());
        form.setTelephone(dto.telephone());
        form.setTuteur(dto.tuteur());
        return form;
    }

    /** Formulaire -> DTO attendu par EntrepriseService.creer() / modifier(). */
    public EntrepriseCreateDto toCreateDto() {
        return new EntrepriseCreateDto(nom, adresse, secteur, email, telephone, tuteur);
    }
}