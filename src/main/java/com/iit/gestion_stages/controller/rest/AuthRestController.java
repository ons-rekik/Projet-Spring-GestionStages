package com.iit.gestion_stages.controller.rest;

import com.iit.gestion_stages.dto.ConnexionDto;
import com.iit.gestion_stages.dto.UtilisateurConnecteDto;
import com.iit.gestion_stages.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthRestController {

    private final AuthService authService;

    @PostMapping("/connexion")
    public UtilisateurConnecteDto connexion(@Valid @RequestBody ConnexionDto dto) {
        return authService.connecter(dto);
    }
}