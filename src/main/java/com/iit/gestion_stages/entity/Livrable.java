package com.iit.gestion_stages.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Getter @Setter @NoArgsConstructor
public class Livrable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le titre est obligatoire")
    private String titre;

    private String lien;
    private LocalDate dateDepot = LocalDate.now();

    @ManyToOne(optional = false)
    private Stage stage;
}