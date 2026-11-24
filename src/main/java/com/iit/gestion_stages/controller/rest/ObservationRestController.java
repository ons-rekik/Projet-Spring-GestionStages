package com.iit.gestion_stages.controller.rest;

import com.iit.gestion_stages.dto.ObservationCreateDto;
import com.iit.gestion_stages.dto.ObservationDto;
import com.iit.gestion_stages.service.ObservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ObservationRestController {

    private final ObservationService observationService;

    @GetMapping("/stages/{stageId}/observations")
    public List<ObservationDto> listerParStage(@PathVariable Long stageId) {
        return observationService.listerParStage(stageId);
    }

    @PostMapping("/stages/{stageId}/observations")
    public ResponseEntity<ObservationDto> ajouter(@PathVariable Long stageId,
                                                  @Valid @RequestBody ObservationCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(observationService.ajouter(stageId, dto));
    }

    @DeleteMapping("/observations/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimer(@PathVariable Long id) {
        observationService.supprimer(id);
    }
}