package com.iit.gestion_stages.repository;

import com.iit.gestion_stages.entity.Reunion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReunionRepository extends JpaRepository<Reunion, Long> {
    List<Reunion> findByStageIdOrderByDateDesc(Long stageId);
}