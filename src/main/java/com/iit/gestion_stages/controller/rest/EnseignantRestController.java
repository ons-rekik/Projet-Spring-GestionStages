package com.iit.gestion_stages.controller.rest;

import com.iit.gestion_stages.dto.EnseignantCreateDto;
import com.iit.gestion_stages.dto.EnseignantDto;
import com.iit.gestion_stages.service.EnseignantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enseignants")
@RequiredArgsConstructor
public class EnseignantRestController {

    private final EnseignantService enseignantService;

    @GetMapping
    public List<EnseignantDto> lister() {
        return enseignantService.lister();
    }

    @GetMapping("/{id}")
    public EnseignantDto obtenir(@PathVariable Long id) {
        return enseignantService.obtenir(id);
    }

    @PostMapping
    public ResponseEntity<EnseignantDto> creer(@Valid @RequestBody EnseignantCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(enseignantService.creer(dto));
    }

    @PutMapping("/{id}")
    public EnseignantDto modifier(@PathVariable Long id, @Valid @RequestBody EnseignantCreateDto dto) {
        return enseignantService.modifier(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimer(@PathVariable Long id) {
        enseignantService.supprimer(id);
    }
}