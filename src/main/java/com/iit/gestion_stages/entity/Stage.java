package com.iit.gestion_stages.entity;

import com.iit.gestion_stages.entity.enums.StatutStage;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor
public class Stage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le sujet est obligatoire")
    private String sujet;

    private LocalDate dateDebut;
    private LocalDate dateFin;

    @Enumerated(EnumType.STRING)
    private StatutStage statut = StatutStage.EN_ATTENTE;

    @ManyToOne(optional = false)
    private Etudiant etudiant;

    @ManyToOne
    private Enseignant encadrant;

    @ManyToOne(optional = false)
    private Entreprise entreprise;

    @OneToMany(mappedBy = "stage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reunion> reunions = new ArrayList<>();

    @OneToMany(mappedBy = "stage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Observation> observations = new ArrayList<>();

    @OneToMany(mappedBy = "stage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EtapeProgression> etapes = new ArrayList<>();

    @OneToMany(mappedBy = "stage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Livrable> livrables = new ArrayList<>();
}