package com.iit.gestion_stages.repository;

import com.iit.gestion_stages.entity.Livrable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LivrableRepository extends JpaRepository<Livrable, Long> {
    List<Livrable> findByStageId(Long stageId);
}