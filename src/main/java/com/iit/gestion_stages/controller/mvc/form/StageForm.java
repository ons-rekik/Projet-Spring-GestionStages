package com.iit.gestion_stages.controller.mvc.form;

import com.iit.gestion_stages.dto.StageCreateDto;
import com.iit.gestion_stages.dto.StageDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * Objet de formulaire SSR pour les stages.
 * Les DTO du projet sont des records : Thymeleaf (th:field) a besoin d'un objet
 * modifiable pour pré-remplir et lier le formulaire.
 */
@Getter
@Setter
@NoArgsConstructor
public class StageForm {

    private Long id;

    @NotBlank(message = "Le sujet est obligatoire")
    private String sujet;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateDebut;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dateFin;

    @NotNull(message = "L'étudiant est obligatoire")
    private Long etudiantId;

    @NotNull(message = "L'entreprise est obligatoire")
    private Long entrepriseId;

    /** StageDto (retourné par le service) -> formulaire d'édition. */
    public static StageForm from(StageDto dto) {
        StageForm form = new StageForm();
        form.setId(dto.id());
        form.setSujet(dto.sujet());
        form.setDateDebut(dto.dateDebut());
        form.setDateFin(dto.dateFin());
        form.setEtudiantId(dto.etudiantId());
        form.setEntrepriseId(dto.entrepriseId());
        return form;
    }

    /** Formulaire -> DTO attendu par StageService.creer() / modifier(). */
    public StageCreateDto toCreateDto() {
        return new StageCreateDto(sujet, dateDebut, dateFin, etudiantId, entrepriseId);
    }
}