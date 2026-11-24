package com.iit.gestion_stages.controller.rest;

import com.iit.gestion_stages.dto.ReunionCreateDto;
import com.iit.gestion_stages.dto.ReunionDto;
import com.iit.gestion_stages.service.ReunionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReunionRestController {

    private final ReunionService reunionService;

    @GetMapping("/stages/{stageId}/reunions")
    public List<ReunionDto> listerParStage(@PathVariable Long stageId) {
        return reunionService.listerParStage(stageId);
    }

    @PostMapping("/stages/{stageId}/reunions")
    public ResponseEntity<ReunionDto> creer(@PathVariable Long stageId,
                                            @Valid @RequestBody ReunionCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reunionService.creer(stageId, dto));
    }

    @GetMapping("/reunions/{id}")
    public ReunionDto obtenir(@PathVariable Long id) {
        return reunionService.obtenir(id);
    }

    @PutMapping("/reunions/{id}")
    public ReunionDto modifier(@PathVariable Long id, @Valid @RequestBody ReunionCreateDto dto) {
        return reunionService.modifier(id, dto);
    }

    @DeleteMapping("/reunions/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimer(@PathVariable Long id) {
        reunionService.supprimer(id);
    }
}