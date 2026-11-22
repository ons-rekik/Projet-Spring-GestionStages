package com.iit.gestion_stages.repository;

import com.iit.gestion_stages.entity.Stage;
import com.iit.gestion_stages.entity.enums.StatutStage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StageRepository extends JpaRepository<Stage, Long> {
    List<Stage> findByEncadrantId(Long enseignantId);
    List<Stage> findByEtudiantId(Long etudiantId);
    List<Stage> findByStatut(StatutStage statut);
    long countByStatut(StatutStage statut);
    List<Stage> findByEncadrantIsNull();
}