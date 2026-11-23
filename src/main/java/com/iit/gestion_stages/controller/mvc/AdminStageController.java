package com.iit.gestion_stages.controller.mvc;

import com.iit.gestion_stages.dto.StageDto;
import com.iit.gestion_stages.dto.StatistiquesDto;
import com.iit.gestion_stages.entity.enums.StatutStage;
import com.iit.gestion_stages.service.StageService;
import com.iit.gestion_stages.service.StatistiquesService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Espace administrateur : consultation des stages et de leurs statistiques.
 */
@Controller
@RequestMapping("/admin/stages")
public class AdminStageController {

    private static final String VUE_LISTE = "admin/stages/liste";

    private final StageService stageService;
    private final StatistiquesService statistiquesService;

    public AdminStageController(StageService stageService,
                                StatistiquesService statistiquesService) {
        this.stageService = stageService;
        this.statistiquesService = statistiquesService;
    }

    @GetMapping
    public String liste(@RequestParam(required = false) StatutStage statut,
                        @RequestParam(required = false) String q,
                        Model model) {
        List<StageDto> stages = (statut != null)
                ? stageService.listerParStatut(statut)
                : stageService.lister();

        if (q != null && !q.isBlank()) {
            String recherche = q.trim().toLowerCase();
            stages = stages.stream()
                    .filter(s -> contient(s.sujet(), recherche)
                            || contient(s.etudiantNomComplet(), recherche)
                            || contient(s.entrepriseNom(), recherche)
                            || contient(s.encadrantNomComplet(), recherche))
                    .toList();
        }

        StatistiquesDto stats = statistiquesService.obtenir();
        model.addAttribute("stages", stages);
        model.addAttribute("stats", stats);
        model.addAttribute("stagesParStatut", stats.stagesParStatut());
        model.addAttribute("statuts", StatutStage.values());
        model.addAttribute("statut", statut);
        model.addAttribute("q", q);
        return VUE_LISTE;
    }

    private boolean contient(String valeur, String recherche) {
        return valeur != null && valeur.toLowerCase().contains(recherche);
    }
}