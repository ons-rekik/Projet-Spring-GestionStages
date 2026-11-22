package com.iit.gestion_stages.entity;

import com.iit.gestion_stages.entity.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor
public class Enseignant extends Utilisateur {

    private String specialite;
    private String departement;

    @OneToMany(mappedBy = "encadrant")
    private List<Stage> stagesEncadres = new ArrayList<>();

    @PrePersist
    void initRole() { setRole(Role.ENSEIGNANT); }
}