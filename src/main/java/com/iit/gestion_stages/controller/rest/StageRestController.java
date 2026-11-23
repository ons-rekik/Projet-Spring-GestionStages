package com.iit.gestion_stages.controller.rest;

import com.iit.gestion_stages.dto.AffectationDto;
import com.iit.gestion_stages.dto.StageCreateDto;
import com.iit.gestion_stages.dto.StageDto;
import com.iit.gestion_stages.dto.StatutStageDto;
import com.iit.gestion_stages.entity.enums.StatutStage;
import com.iit.gestion_stages.service.StageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stages")
@RequiredArgsConstructor
public class StageRestController {

    private final StageService stageService;

    @GetMapping
    public List<StageDto> lister(@RequestParam(required = false) StatutStage statut,
                                 @RequestParam(required = false) Long etudiantId,
                                 @RequestParam(required = false) Long encadrantId,
                                 @RequestParam(required = false, defaultValue = "false") boolean sansEncadrant) {
        if (statut != null) {
            return stageService.listerParStatut(statut);
        }
        if (etudiantId != null) {
            return stageService.listerParEtudiant(etudiantId);
        }
        if (encadrantId != null) {
            return stageService.listerParEncadrant(encadrantId);
        }
        if (sansEncadrant) {
            return stageService.listerSansEncadrant();
        }
        return stageService.lister();
    }

    @GetMapping("/{id}")
    public StageDto obtenir(@PathVariable Long id) {
        return stageService.obtenir(id);
    }

    @PostMapping
    public ResponseEntity<StageDto> creer(@Valid @RequestBody StageCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(stageService.creer(dto));
    }

    @PutMapping("/{id}")
    public StageDto modifier(@PathVariable Long id, @Valid @RequestBody StageCreateDto dto) {
        return stageService.modifier(id, dto);
    }

    @PutMapping("/{id}/encadrant")
    public StageDto affecterEncadrant(@PathVariable Long id, @Valid @RequestBody AffectationDto dto) {
        return stageService.affecterEncadrant(id, dto);
    }

    @PutMapping("/{id}/statut")
    public StageDto changerStatut(@PathVariable Long id, @Valid @RequestBody StatutStageDto dto) {
        return stageService.changerStatut(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimer(@PathVariable Long id) {
        stageService.supprimer(id);
    }
}