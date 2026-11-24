package com.iit.gestion_stages.controller.mvc.form;

import com.iit.gestion_stages.dto.EtudiantCreateDto;
import com.iit.gestion_stages.dto.EtudiantDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Objet de formulaire SSR pour les étudiants.
 * Les DTO du projet sont des records : Thymeleaf (th:field) a besoin d'un objet
 * modifiable pour pré-remplir et lier le formulaire.
 */
@Getter
@Setter
@NoArgsConstructor
public class EtudiantForm {

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

    private String matricule;
    private String filiere;
    private String niveau;

    /** EtudiantDto (retourné par le service) -> formulaire d'édition. */
    public static EtudiantForm from(EtudiantDto dto) {
        EtudiantForm form = new EtudiantForm();
        form.setId(dto.id());
        form.setNom(dto.nom());
        form.setPrenom(dto.prenom());
        form.setEmail(dto.email());
        form.setMatricule(dto.matricule());
        form.setFiliere(dto.filiere());
        form.setNiveau(dto.niveau());
        return form;
    }

    /** Formulaire -> DTO attendu par EtudiantService.creer() / modifier(). */
    public EtudiantCreateDto toCreateDto() {
        return new EtudiantCreateDto(nom, prenom, email, motDePasse, matricule, filiere, niveau);
    }
}