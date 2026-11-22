package com.iit.gestion_stages.repository;

import com.iit.gestion_stages.entity.EtapeProgression;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EtapeProgressionRepository extends JpaRepository<EtapeProgression, Long> {
    List<EtapeProgression> findByStageId(Long stageId);
}