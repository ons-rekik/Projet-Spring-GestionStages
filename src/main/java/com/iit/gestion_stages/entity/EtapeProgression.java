package com.iit.gestion_stages.entity;

import com.iit.gestion_stages.entity.enums.StatutEtape;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor
public class EtapeProgression {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le titre est obligatoire")
    private String titre;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    private StatutEtape statut = StatutEtape.A_FAIRE;

    private boolean validee = false;

    @ManyToOne(optional = false)
    private Stage stage;
}