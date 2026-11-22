package com.iit.gestion_stages.entity;

import com.iit.gestion_stages.entity.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor
public class Etudiant extends Utilisateur {

    private String matricule;
    private String filiere;
    private String niveau;

    @OneToMany(mappedBy = "etudiant")
    private List<Stage> stages = new ArrayList<>();

    @PrePersist
    void initRole() { setRole(Role.ETUDIANT); }
}