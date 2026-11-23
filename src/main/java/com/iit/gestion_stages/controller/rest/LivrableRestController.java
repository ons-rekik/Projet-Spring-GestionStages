package com.iit.gestion_stages.controller.rest;

import com.iit.gestion_stages.dto.LivrableCreateDto;
import com.iit.gestion_stages.dto.LivrableDto;
import com.iit.gestion_stages.service.LivrableService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LivrableRestController {

    private final LivrableService livrableService;

    @GetMapping("/stages/{stageId}/livrables")
    public List<LivrableDto> listerParStage(@PathVariable Long stageId) {
        return livrableService.listerParStage(stageId);
    }

    @PostMapping("/stages/{stageId}/livrables")
    public ResponseEntity<LivrableDto> deposer(@PathVariable Long stageId,
                                               @Valid @RequestBody LivrableCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(livrableService.deposer(stageId, dto));
    }

    @DeleteMapping("/livrables/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimer(@PathVariable Long id) {
        livrableService.supprimer(id);
    }
}