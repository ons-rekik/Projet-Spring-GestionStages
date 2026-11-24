package com.iit.gestion_stages.controller.rest;

import com.iit.gestion_stages.dto.AvancementDto;
import com.iit.gestion_stages.dto.EtapeCreateDto;
import com.iit.gestion_stages.dto.EtapeDto;
import com.iit.gestion_stages.service.EtapeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EtapeRestController {

    private final EtapeService etapeService;

    @GetMapping("/stages/{stageId}/etapes")
    public List<EtapeDto> listerParStage(@PathVariable Long stageId) {
        return etapeService.listerParStage(stageId);
    }

    @PostMapping("/stages/{stageId}/etapes")
    public ResponseEntity<EtapeDto> creer(@PathVariable Long stageId,
                                          @Valid @RequestBody EtapeCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(etapeService.creer(stageId, dto));
    }

    @PutMapping("/etapes/{id}/avancement")
    public EtapeDto declarerAvancement(@PathVariable Long id, @Valid @RequestBody AvancementDto dto) {
        return etapeService.declarerAvancement(id, dto);
    }

    @PutMapping("/etapes/{id}/validation")
    public EtapeDto valider(@PathVariable Long id) {
        return etapeService.valider(id);
    }

    @DeleteMapping("/etapes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimer(@PathVariable Long id) {
        etapeService.supprimer(id);
    }
}