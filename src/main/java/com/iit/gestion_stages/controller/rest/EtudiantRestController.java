package com.iit.gestion_stages.controller.rest;

import com.iit.gestion_stages.dto.EtudiantCreateDto;
import com.iit.gestion_stages.dto.EtudiantDto;
import com.iit.gestion_stages.service.EtudiantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/etudiants")
@RequiredArgsConstructor
public class EtudiantRestController {

    private final EtudiantService etudiantService;

    @GetMapping
    public List<EtudiantDto> lister(@RequestParam(required = false) String filiere) {
        if (filiere != null && !filiere.isBlank()) {
            return etudiantService.listerParFiliere(filiere);
        }
        return etudiantService.lister();
    }

    @GetMapping("/{id}")
    public EtudiantDto obtenir(@PathVariable Long id) {
        return etudiantService.obtenir(id);
    }

    @PostMapping
    public ResponseEntity<EtudiantDto> creer(@Valid @RequestBody EtudiantCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(etudiantService.creer(dto));
    }

    @PutMapping("/{id}")
    public EtudiantDto modifier(@PathVariable Long id, @Valid @RequestBody EtudiantCreateDto dto) {
        return etudiantService.modifier(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimer(@PathVariable Long id) {
        etudiantService.supprimer(id);
    }
}