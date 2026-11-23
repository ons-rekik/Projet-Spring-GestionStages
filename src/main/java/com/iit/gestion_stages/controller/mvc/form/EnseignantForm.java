package com.iit.gestion_stages.controller.mvc.form;

import com.iit.gestion_stages.dto.EnseignantCreateDto;
import com.iit.gestion_stages.dto.EnseignantDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Objet de formulaire SSR pour les enseignants.
 * Les DTO du projet sont des records : Thymeleaf (th:field) a besoin d'un objet
 * modifiable pour pré-remplir et lier le formulaire.
 */
@Getter
@Setter
@NoArgsConstructor
public class EnseignantForm {

    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Email invalide")
    private String email;

    /** Facultatif en modification : s'il est vide, l'ancien mot de passe est conservé. */
    private String motDePasse;

    private String specialite;
    private String departement;

    /** EnseignantDto (retourné par le service) -> formulaire d'édition. */
    public static EnseignantForm from(EnseignantDto dto) {
        EnseignantForm form = new EnseignantForm();
        form.setId(dto.id());
        form.setNom(dto.nom());
        form.setPrenom(dto.prenom());
        form.setEmail(dto.email());
        form.setSpecialite(dto.specialite());
        form.setDepartement(dto.departement());
        return form;
    }

    /** Formulaire -> DTO attendu par EnseignantService.creer() / modifier(). */
    public EnseignantCreateDto toCreateDto() {
        return new EnseignantCreateDto(nom, prenom, email, motDePasse, specialite, departement);
    }
}