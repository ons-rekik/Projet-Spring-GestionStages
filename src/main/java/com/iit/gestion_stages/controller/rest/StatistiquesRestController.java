package com.iit.gestion_stages.controller.rest;

import com.iit.gestion_stages.dto.StatistiquesDto;
import com.iit.gestion_stages.service.StatistiquesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statistiques")
@RequiredArgsConstructor
public class StatistiquesRestController {

    private final StatistiquesService statistiquesService;

    @GetMapping
    public StatistiquesDto obtenir() {
        return statistiquesService.obtenir();
    }
}