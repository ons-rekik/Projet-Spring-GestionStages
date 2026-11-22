package com.iit.gestion_stages.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor
public class Entreprise {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    private String adresse;
    private String secteur;

    @Email
    private String email;

    private String telephone;
    private String tuteur;
}