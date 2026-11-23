package com.iit.gestion_stages.controller.mvc;

import com.iit.gestion_stages.dto.AffectationDto;
import com.iit.gestion_stages.dto.StageDto;
import com.iit.gestion_stages.exception.ResourceNotFoundException;
import com.iit.gestion_stages.service.EnseignantService;
import com.iit.gestion_stages.service.StageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Espace administrateur : affectation d'un enseignant encadrant à un étudiant
 * (via son stage).
 */
@Controller
@RequestMapping("/admin/affectations")
public class AdminAffectationController {

    private static final String VUE_LISTE = "admin/affectations/liste";
    private static final String REDIRECT_LISTE = "redirect:/admin/affectations";

    private final StageService stageService;
    private final EnseignantService enseignantService;

    public AdminAffectationController(StageService stageService,
                                      EnseignantService enseignantService) {
        this.stageService = stageService;
        this.enseignantService = enseignantService;
    }

    @GetMapping
    public String liste(@RequestParam(defaultValue = "false") boolean nonAffectes, Model model) {
        List<StageDto> stages = nonAffectes
                ? stageService.listerSansEncadrant()
                : stageService.lister();
        model.addAttribute("stages", stages);
        model.addAttribute("enseignants", enseignantService.lister());
        model.addAttribute("nonAffectes", nonAffectes);
        return VUE_LISTE;
    }

    @PostMapping("/{stageId}")
    public String affecter(@PathVariable Long stageId,
                           @RequestParam(required = false) Long enseignantId,
                           RedirectAttributes ra) {
        if (enseignantId == null) {
            ra.addFlashAttribute("error", "Veuillez choisir un enseignant");
            return REDIRECT_LISTE;
        }
        try {
            stageService.affecterEncadrant(stageId, new AffectationDto(enseignantId));
            ra.addFlashAttribute("success", "Enseignant affecté avec succès");
        } catch (ResourceNotFoundException ex) {
            ra.addFlashAttribute("error", "Stage ou enseignant introuvable");
        }
        return REDIRECT_LISTE;
    }
}