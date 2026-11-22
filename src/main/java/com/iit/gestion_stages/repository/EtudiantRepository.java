package com.iit.gestion_stages.repository;

import com.iit.gestion_stages.entity.Etudiant;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {
    Optional<Etudiant> findByEmail(String email);
    List<Etudiant> findByFiliere(String filiere);
}