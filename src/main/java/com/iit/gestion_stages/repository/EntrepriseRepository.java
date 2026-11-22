package com.iit.gestion_stages.repository;

import com.iit.gestion_stages.entity.Entreprise;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EntrepriseRepository extends JpaRepository<Entreprise, Long> {
    List<Entreprise> findByNomContainingIgnoreCase(String nom);
}