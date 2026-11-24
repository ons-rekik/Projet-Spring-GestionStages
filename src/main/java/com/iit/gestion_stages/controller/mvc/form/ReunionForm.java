package com.iit.gestion_stages.controller.mvc.form;

import com.iit.gestion_stages.dto.ReunionCreateDto;
import com.iit.gestion_stages.dto.ReunionDto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * Objet de formulaire SSR pour les réunions de suivi.
 * Le format de la date correspond à celui de {@code <input type="datetime-local">}.
 */
@Getter
@Setter
@NoArgsConstructor
public class ReunionForm {

    private Long id;

    @NotNull(message = "La date est obligatoire")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime date;

    private String lieu;

    @Size(max = 2000, message = "Le compte rendu ne doit pas dépasser 2000 caractères")
    private String compteRendu;

    /** ReunionDto (retourné par le service) -> formulaire d'édition. */
    public static ReunionForm from(ReunionDto dto) {
        ReunionForm form = new ReunionForm();
        form.setId(dto.id());
        form.setDate(dto.date());
        form.setLieu(dto.lieu());
        form.setCompteRendu(dto.compteRendu());
        return form;
    }

    /** Formulaire -> DTO attendu par ReunionService.creer() / modifier(). */
    public ReunionCreateDto toCreateDto() {
        return new ReunionCreateDto(date, lieu, compteRendu);
    }
}