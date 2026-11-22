package com.iit.gestion_stages.repository;

import com.iit.gestion_stages.entity.Observation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ObservationRepository extends JpaRepository<Observation, Long> {
    List<Observation> findByStageId(Long stageId);
}