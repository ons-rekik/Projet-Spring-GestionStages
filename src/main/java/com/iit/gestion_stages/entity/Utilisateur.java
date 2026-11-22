package com.iit.gestion_stages.entity;

import com.iit.gestion_stages.entity.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter @Setter @NoArgsConstructor
public abstract class Utilisateur {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;

    @Email @NotBlank(message = "L'email est obligatoire")
    @Column(unique = true)
    private String email;

    private String motDePasse;

    @Enumerated(EnumType.STRING)
    private Role role;
}