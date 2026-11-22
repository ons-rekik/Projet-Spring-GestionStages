package com.iit.gestion_stages.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Getter @Setter @NoArgsConstructor
public class Observation {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le contenu est obligatoire")
    @Column(length = 2000)
    private String contenu;

    @Column(length = 2000)
    private String recommandation;

    private LocalDate date = LocalDate.now();

    @ManyToOne(optional = false)
    private Stage stage;

    @ManyToOne
    private Enseignant auteur;
}