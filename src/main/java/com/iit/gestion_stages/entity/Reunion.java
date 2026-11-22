package com.iit.gestion_stages.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor
public class Reunion {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime date;
    private String lieu;

    @Column(length = 2000)
    private String compteRendu;

    @ManyToOne(optional = false)
    private Stage stage;
}