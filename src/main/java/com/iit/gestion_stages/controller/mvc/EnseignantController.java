package com.iit.gestion_stages.controller.mvc;

import com.iit.gestion_stages.dto.EnseignantDto;
import com.iit.gestion_stages.dto.EtudiantDto;
import com.iit.gestion_stages.dto.StageDto;
import com.iit.gestion_stages.exception.ResourceNotFoundException;
import com.iit.gestion_stages.service.EnseignantService;
import com.iit.gestion_stages.service.EtudiantService;
import com.iit.gestion_stages.service.StageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Espace enseignant encadrant.
 * Fonctionnalité 1 : consulter les étudiants affectés.
 *
 * Le projet n'a pas encore d'authentification : l'enseignant concerné est donc
 * choisi dans une liste déroulante (paramètre {@code enseignantId}).
 */
@Controller
@RequestMapping("/enseignant")
public class EnseignantController {

    private static final String VUE_MES_ETUDIANTS = "enseignant/mes-etudiants";

    private final EnseignantService enseignantService;
    private final StageService stageService;
    private final EtudiantService etudiantService;

    public EnseignantController(EnseignantService enseignantService,
                                StageService stageService,
                                EtudiantService etudiantService) {
        this.enseignantService = enseignantService;
        this.stageService = stageService;
        this.etudiantService = etudiantService;
    }

    @GetMapping("/etudiants")
    public String mesEtudiants(@RequestParam(required = false) Long enseignantId, Model model) {
        model.addAttribute("enseignants", enseignantService.lister());
        model.addAttribute("enseignantId", enseignantId);

        if (enseignantId != null) {
            try {
                EnseignantDto enseignant = enseignantService.obtenir(enseignantId);
                // un stage par étudiant affecté à cet enseignant
                List<StageDto> stages = stageService.listerParEncadrant(enseignantId);

                // détails de chaque étudiant (matricule, filière, niveau, email)
                Map<Long, EtudiantDto> etudiants = new HashMap<>();
                for (StageDto stage : stages) {
                    etudiants.computeIfAbsent(stage.etudiantId(), etudiantService::obtenir);
                }

                model.addAttribute("enseignant", enseignant);
                model.addAttribute("stages", stages);
                model.addAttribute("etudiants", etudiants);
            } catch (ResourceNotFoundException ex) {
                model.addAttribute("error", "Enseignant introuvable");
            }
        }
        return VUE_MES_ETUDIANTS;
    }
}