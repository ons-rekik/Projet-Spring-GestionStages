package com.iit.gestion_stages.controller.rest;

import com.iit.gestion_stages.dto.EntrepriseCreateDto;
import com.iit.gestion_stages.dto.EntrepriseDto;
import com.iit.gestion_stages.service.EntrepriseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/entreprises")
@RequiredArgsConstructor
public class EntrepriseRestController {

    private final EntrepriseService entrepriseService;

    @GetMapping
    public List<EntrepriseDto> lister(@RequestParam(required = false) String nom) {
        if (nom != null && !nom.isBlank()) {
            return entrepriseService.rechercherParNom(nom);
        }
        return entrepriseService.lister();
    }

    @GetMapping("/{id}")
    public EntrepriseDto obtenir(@PathVariable Long id) {
        return entrepriseService.obtenir(id);
    }

    @PostMapping
    public ResponseEntity<EntrepriseDto> creer(@Valid @RequestBody EntrepriseCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(entrepriseService.creer(dto));
    }

    @PutMapping("/{id}")
    public EntrepriseDto modifier(@PathVariable Long id, @Valid @RequestBody EntrepriseCreateDto dto) {
        return entrepriseService.modifier(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimer(@PathVariable Long id) {
        entrepriseService.supprimer(id);
    }
}